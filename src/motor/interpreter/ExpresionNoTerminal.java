package motor.interpreter;

public abstract class ExpresionNoTerminal implements Expresion {
    private final Expresion izquierda;
    private final Expresion derecha;

    protected ExpresionNoTerminal(Expresion izquierda, Expresion derecha) {
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    protected abstract double operar(double a, double b);

    @Override
    public final Object interpretar(Contexto contexto) {
        return operar(aNumero(izquierda.interpretar(contexto)), aNumero(derecha.interpretar(contexto)));
    }

    private static double aNumero(Object valor) {
        if (valor instanceof Number n) {
            return n.doubleValue();
        }
        throw new IllegalArgumentException("No se puede operar con un valor no numérico: " + valor);
    }
}
