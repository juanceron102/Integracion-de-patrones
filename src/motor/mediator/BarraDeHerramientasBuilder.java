package motor.mediator;

import java.util.List;

public class BarraDeHerramientasBuilder extends ComponenteEditor {
    private boolean iconosHabilitados = true;

    public BarraDeHerramientasBuilder(DocumentEditorMediator mediator) {
        super(mediator);
    }

    public void seleccionarPlantilla(String plantilla) {
        log("Usuario selecciona plantilla " + plantilla);
        mediator.notificar(this, TipoEvento.PLANTILLA_CAMBIADA, plantilla);
    }

    public void agregarEncabezado(String texto) {
        mediator.notificar(this, TipoEvento.ENCABEZADO_AGREGADO, texto);
    }

    public void agregarParrafo(String texto) {
        mediator.notificar(this, TipoEvento.PARRAFO_AGREGADO, texto);
    }

    public void agregarTabla(List<String> cabecera, List<List<String>> filas) {
        mediator.notificar(this, TipoEvento.TABLA_AGREGADA, new DatosTabla(cabecera, filas));
    }

    public void agregarPie(String texto) {
        mediator.notificar(this, TipoEvento.PIE_AGREGADO, texto);
    }

    public void setIconosHabilitados(boolean habilitados) {
        if (iconosHabilitados != habilitados) {
            log("Herramienta de iconos " + (habilitados ? "habilitada" : "deshabilitada"));
        }
        iconosHabilitados = habilitados;
    }

    public boolean isIconosHabilitados() {
        return iconosHabilitados;
    }

    public record DatosTabla(List<String> cabecera, List<List<String>> filas) {
    }
}
