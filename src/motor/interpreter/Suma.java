package motor.interpreter;

public class Suma extends ExpresionNoTerminal {

    public Suma(Expresion izquierda, Expresion derecha) {
        super(izquierda, derecha);
    }

    @Override
    protected double operar(double a, double b) {
        return a + b;
    }
}
