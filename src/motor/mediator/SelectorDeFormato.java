package motor.mediator;

public class SelectorDeFormato extends ComponenteEditor {

    public SelectorDeFormato(DocumentEditorMediator mediator) {
        super(mediator);
    }

    // PDF HTML o MARKDOWN
    public void seleccionarFormato(String formato) {
        log("Usuario selecciona formato " + formato);
        mediator.notificar(this, TipoEvento.FORMATO_CAMBIADO, formato);
    }

    /** PAGINADO o CONTINUO. */
    public void seleccionarDisposicion(String disposicion) {
        log("Usuario selecciona disposicion " + disposicion);
        mediator.notificar(this, TipoEvento.DISPOSICION_CAMBIADA, disposicion);
    }
}
