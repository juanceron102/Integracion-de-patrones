package motor.mediator;

public abstract class ComponenteEditor {
    protected final DocumentEditorMediator mediator;

    protected ComponenteEditor(DocumentEditorMediator mediator) {
        this.mediator = mediator;
    }

    protected void log(String mensaje) {
        System.out.println("  (" + getClass().getSimpleName() + ") " + mensaje);
    }
}
