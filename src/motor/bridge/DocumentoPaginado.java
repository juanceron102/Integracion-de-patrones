package motor.bridge;

import motor.builder.ContenidoDocumento;
import motor.flyweight.GlifoFactory;

public class DocumentoPaginado extends Documento {
    private final double altoPagina;
    private int paginaActual;

    public DocumentoPaginado(ContenidoDocumento contenido, GlifoFactory glifos, RenderizadorEngine engine,
            double altoPagina) {
        super(contenido, glifos, engine);
        this.altoPagina = altoPagina;
    }

    public DocumentoPaginado(ContenidoDocumento contenido, GlifoFactory glifos, RenderizadorEngine engine) {
        this(contenido, glifos, engine, 842); 
    }

    @Override
    protected void comenzar() {
        paginaActual = 1;
        cursorY = MARGEN;
        engine.iniciarPagina(paginaActual);
    }

    @Override
    protected void reservarEspacio(double alto) {
        if (cursorY + alto > altoPagina - MARGEN && cursorY > MARGEN) {
            engine.finalizarPagina(paginaActual);
            paginaActual++;
            cursorY = MARGEN;
            engine.iniciarPagina(paginaActual);
        }
    }

    @Override
    protected void terminar() {
        engine.finalizarPagina(paginaActual);
    }
}
