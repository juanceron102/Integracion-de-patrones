package motor.bridge;

import motor.builder.ContenidoDocumento;
import motor.flyweight.GlifoFactory;

public class DocumentoContinuo extends Documento {

    public DocumentoContinuo(ContenidoDocumento contenido, GlifoFactory glifos, RenderizadorEngine engine) {
        super(contenido, glifos, engine);
    }

    @Override
    protected void comenzar() {
        cursorY = MARGEN;
    }

    @Override
    protected void reservarEspacio(double alto) {
        // sin paginacion el contenido fluye hacia abajo
    }

    @Override
    protected void terminar() {
    }
}
