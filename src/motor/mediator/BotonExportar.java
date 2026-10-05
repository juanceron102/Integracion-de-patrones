package motor.mediator;

public class BotonExportar extends ComponenteEditor {
    private boolean habilitado;

    public BotonExportar(DocumentEditorMediator mediator) {
        super(mediator);
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public void click() {
        if (!habilitado) {
            log("Boton deshabilitado: seleccione plantilla y formato primero");
            return;
        }
        log("Usuario presiona Exportar");
        mediator.notificar(this, TipoEvento.EXPORTAR, null);
    }
}
