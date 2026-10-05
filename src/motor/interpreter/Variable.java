package motor.interpreter;

public class Variable extends ExpresionTerminal {
    private final String nombre;

    public Variable(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public Object interpretar(Contexto contexto) {
        return contexto.obtener(nombre);
    }
}
