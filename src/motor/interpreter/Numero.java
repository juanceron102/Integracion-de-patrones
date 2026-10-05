package motor.interpreter;

public class Numero extends ExpresionTerminal {
    private final double valor;

    public Numero(double valor) {
        this.valor = valor;
    }

    @Override
    public Object interpretar(Contexto contexto) {
        return valor;
    }
}
