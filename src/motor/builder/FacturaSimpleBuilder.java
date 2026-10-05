package motor.builder;

import motor.flyweight.*;

public class FacturaSimpleBuilder extends AbstractDocumentBuilder {

    public FacturaSimpleBuilder(GlifoFactory glifos) {
        super(glifos);
    }

    @Override
    protected Fuente fuenteTitulo() {
        return glifos.getFuente("Monospace", true);
    }

    @Override
    protected Fuente fuenteCuerpo() {
        return glifos.getFuente("Monospace", false);
    }

    @Override
    protected Icono iconoEncabezado() {
        return glifos.getIcono("factura");
    }

    @Override
    protected Icono iconoParrafo() {
        return null;
    }

    @Override
    protected Icono iconoTabla() {
        return glifos.getIcono("check");
    }

    @Override
    protected String decorarPie(String texto) {
        return texto + " - Gracias por su compra";
    }

    @Override
    public String getNombrePlantilla() {
        return "Factura simple";
    }
}
