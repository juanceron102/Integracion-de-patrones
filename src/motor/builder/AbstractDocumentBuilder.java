package motor.builder;

import java.util.ArrayList;
import java.util.List;

import motor.flyweight.*;

public abstract class AbstractDocumentBuilder implements DocumentBuilder {
    protected final GlifoFactory glifos;
    private final List<ElementoDocumento> partes = new ArrayList<>();
    private boolean incluirIconos = true;

    protected AbstractDocumentBuilder(GlifoFactory glifos) {
        this.glifos = glifos;
    }

    protected abstract Fuente fuenteTitulo();

    protected abstract Fuente fuenteCuerpo();

    protected abstract Icono iconoEncabezado();

    protected abstract Icono iconoParrafo();

    protected abstract Icono iconoTabla();

    protected String decorarPie(String texto) {
        return texto;
    }

    @Override
    public DocumentBuilder reset() {
        partes.clear();
        return this;
    }

    @Override
    public DocumentBuilder addHeader(String texto) {
        partes.add(new ElementoDocumento(TipoElemento.ENCABEZADO, texto, fuenteTitulo(), iconoEncabezado()));
        return this;
    }

    @Override
    public DocumentBuilder addParagraph(String texto) {
        partes.add(new ElementoDocumento(TipoElemento.PARRAFO, texto, fuenteCuerpo(), iconoParrafo()));
        return this;
    }

    @Override
    public DocumentBuilder addTable(List<String> cabecera, List<List<String>> filas) {
        partes.add(new Tabla(cabecera, filas, fuenteCuerpo(), iconoTabla()));
        return this;
    }

    @Override
    public DocumentBuilder addFooter(String texto) {
        partes.add(new ElementoDocumento(TipoElemento.PIE, decorarPie(texto), fuenteCuerpo(), null));
        return this;
    }

    @Override
    public void setIncluirIconos(boolean incluirIconos) {
        this.incluirIconos = incluirIconos;
    }

    @Override
    public ContenidoDocumento build() {
        if (partes.isEmpty()) {
            throw new IllegalStateException("El documento no tiene contenido");
        }
        List<ElementoDocumento> copia = new ArrayList<>();
        partes.forEach(p -> copia.add(p.copiar(incluirIconos)));
        String titulo = partes.stream()
                .filter(p -> p.getTipo() == TipoElemento.ENCABEZADO)
                .map(ElementoDocumento::getTexto)
                .findFirst()
                .orElse(getNombrePlantilla());
        return new ContenidoDocumento(titulo, getNombrePlantilla(), copia);
    }
}
