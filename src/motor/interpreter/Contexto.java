package motor.interpreter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Contexto {
    private final Map<String, Object> variables = new HashMap<>();

    public Contexto() {
        variables.put("FECHA_ACTUAL", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    public void definir(String nombre, Object valor) {
        variables.put(nombre, valor);
    }

    public Object obtener(String nombre) {
        if (!variables.containsKey(nombre)) {
            throw new IllegalArgumentException("Variable no definida: " + nombre);
        }
        return variables.get(nombre);
    }
}
