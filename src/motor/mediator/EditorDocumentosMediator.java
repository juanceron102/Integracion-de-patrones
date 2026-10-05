package motor.mediator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;

import motor.bridge.*;
import motor.builder.*;
import motor.chain.*;
import motor.flyweight.GlifoFactory;


public class EditorDocumentosMediator implements DocumentEditorMediator {
    private static final Path CARPETA_SALIDA = Path.of("salida");

    private final GlifoFactory glifos;
    private final ProcesadorHandler cadena;

    private SelectorDeFormato selector;
    private BarraDeHerramientasBuilder barra;
    private VistaPrevia vista;
    private BotonExportar boton;

    private DocumentBuilder builder;
    private RenderizadorEngine engine;
    private String disposicion = "PAGINADO";

    public EditorDocumentosMediator(GlifoFactory glifos, ProcesadorHandler cadena) {
        this.glifos = glifos;
        this.cadena = cadena;
    }

    public void registrar(SelectorDeFormato selector, BarraDeHerramientasBuilder barra, VistaPrevia vista,
            BotonExportar boton) {
        this.selector = selector;
        this.barra = barra;
        this.vista = vista;
        this.boton = boton;
        boton.setHabilitado(false);
    }

    @Override
    public void notificar(ComponenteEditor emisor, TipoEvento evento, Object dato) {
        switch (evento) {
            case FORMATO_CAMBIADO -> cambiarFormato((String) dato);
            case DISPOSICION_CAMBIADA -> {
                disposicion = (String) dato;
                vista.mostrarEstado("Disposicion " + disposicion);
            }
            case PLANTILLA_CAMBIADA -> cambiarPlantilla((String) dato);
            case ENCABEZADO_AGREGADO -> conBuilder(() -> builder.addHeader((String) dato));
            case PARRAFO_AGREGADO -> conBuilder(() -> builder.addParagraph((String) dato));
            case TABLA_AGREGADA -> conBuilder(() -> {
                BarraDeHerramientasBuilder.DatosTabla t = (BarraDeHerramientasBuilder.DatosTabla) dato;
                builder.addTable(t.cabecera(), t.filas());
            });
            case PIE_AGREGADO -> conBuilder(() -> builder.addFooter((String) dato));
            case EXPORTAR -> exportar();
        }
    }

    private void cambiarFormato(String formato) {
        engine = switch (formato.toUpperCase()) {
            case "PDF" -> new PdfRenderEngine();
            case "HTML" -> new HtmlRenderEngine();
            case "MARKDOWN" -> new MarkdownRenderEngine();
            default -> throw new IllegalArgumentException("Formato no soportado: " + formato);
        };
        aplicarConfiguracionIconos();
        vista.mostrarEstado("Renderizador " + engine.getClass().getSimpleName());
        actualizarBoton();
    }

    private void cambiarPlantilla(String plantilla) {
        builder = switch (plantilla.toUpperCase()) {
            case "FACTURA" -> new FacturaSimpleBuilder(glifos);
            case "REPORTE" -> new ReporteEjecutivoBuilder(glifos);
            default -> throw new IllegalArgumentException("Plantilla no soportada: " + plantilla);
        };
        aplicarConfiguracionIconos();
        vista.mostrarEstado("Builder " + builder.getClass().getSimpleName());
        actualizarBoton();
    }

    private void aplicarConfiguracionIconos() {
        boolean iconos = !(engine instanceof MarkdownRenderEngine);
        barra.setIconosHabilitados(iconos);
        if (builder != null) {
            builder.setIncluirIconos(iconos);
        }
    }

    private void actualizarBoton() {
        boton.setHabilitado(builder != null && engine != null);
    }

    private void conBuilder(Runnable accion) {
        if (builder == null) {
            vista.mostrarEstado("Seleccione una plantilla antes de editar");
            return;
        }
        accion.run();
    }

    private void exportar() {
        ContenidoDocumento contenido = builder.build();
        RegistroProcesamiento registro = new RegistroProcesamiento();
        try {
            cadena.procesar(contenido, registro);
        } catch (ErrorCriticoException e) {
            vista.mostrarRegistro(registro.getMensajes());
            vista.mostrarError(e.getMessage());
            return;
        }
        vista.mostrarRegistro(registro.getMensajes());

        Documento documento = "CONTINUO".equalsIgnoreCase(disposicion)
                ? new DocumentoContinuo(contenido, glifos, engine)
                : new DocumentoPaginado(contenido, glifos, engine);
        String resultado = documento.renderizar();
        if (!engine.isBinario()) {
            vista.mostrar(engine.getFormato() + " / " + documento.getClass().getSimpleName(), resultado);
        }
        try {
            vista.mostrarArchivo(guardar(contenido, resultado));
        } catch (IOException e) {
            vista.mostrarError("No se pudo guardar el archivo: " + e.getMessage());
        }
    }

    private Path guardar(ContenidoDocumento contenido, String resultado) throws IOException {
        String nombre = Normalizer.normalize(contenido.getPlantilla(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                + "-" + disposicion.toLowerCase() + "." + engine.getExtension();
        Path archivo = CARPETA_SALIDA.resolve(nombre);
        Files.createDirectories(CARPETA_SALIDA);
        Files.write(archivo, resultado.getBytes(engine.getCharset()));
        return archivo;
    }
}
