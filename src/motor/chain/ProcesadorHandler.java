package motor.chain;

import motor.builder.ContenidoDocumento;

public abstract class ProcesadorHandler {
    private ProcesadorHandler siguiente;

    public ProcesadorHandler enlazar(ProcesadorHandler siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    public final void procesar(ContenidoDocumento documento, RegistroProcesamiento registro)
            throws ErrorCriticoException {
        manejar(documento, registro);
        if (siguiente != null) {
            siguiente.procesar(documento, registro);
        }
    }

    protected abstract void manejar(ContenidoDocumento documento, RegistroProcesamiento registro)
            throws ErrorCriticoException;

    protected String nombre() {
        return getClass().getSimpleName();
    }
}
