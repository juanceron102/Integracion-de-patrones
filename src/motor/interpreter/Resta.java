package motor.interpreter;

public class Resta extends ExpresionNoTerminal {

    public Resta(Expresion izquierda, Expresion derecha) {
        super(izquierda, derecha);
    }

    @Override
    protected double operar(double a, double b) {
        return a - b;
    }
}
