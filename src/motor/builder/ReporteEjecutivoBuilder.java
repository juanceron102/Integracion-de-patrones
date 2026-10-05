package motor.builder;

import motor.flyweight.*;

public class ReporteEjecutivoBuilder extends AbstractDocumentBuilder {

    public ReporteEjecutivoBuilder(GlifoFactory glifos) {
        super(glifos);
    }

    @Override
    protected Fuente fuenteTitulo() {
        return glifos.getFuente("Serif", true);
    }

    @Override
    protected Fuente fuenteCuerpo() {
        return glifos.getFuente("Serif", false);
    }

    @Override
    protected Icono iconoEncabezado() {
        return glifos.getIcono("logo");
    }

    @Override
    protected Icono iconoParrafo() {
        return glifos.getIcono("vineta");
    }

    @Override
    protected Icono iconoTabla() {
        return glifos.getIcono("grafico");
    }

    @Override
    protected String decorarPie(String texto) {
        return "Reporte ejecutivo | " + texto;
    }

    @Override
    public String getNombrePlantilla() {
        return "Reporte ejecutivo";
    }
}
