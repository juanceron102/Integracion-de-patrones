package motor.mediator;

public interface DocumentEditorMediator {

    void notificar(ComponenteEditor emisor, TipoEvento evento, Object dato);
}
