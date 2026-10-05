package motor.interpreter;

public class Multiplicacion extends ExpresionNoTerminal {

    public Multiplicacion(Expresion izquierda, Expresion derecha) {
        super(izquierda, derecha);
    }

    @Override
    protected double operar(double a, double b) {
        return a * b;
    }
}
