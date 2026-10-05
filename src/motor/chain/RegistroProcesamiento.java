package motor.chain;

import java.util.ArrayList;
import java.util.List;

public class RegistroProcesamiento {
    private final List<String> mensajes = new ArrayList<>();

    public void agregar(String manejador, String mensaje) {
        mensajes.add("[" + manejador + "] " + mensaje);
    }

    public List<String> getMensajes() {
        return mensajes;
    }
}
