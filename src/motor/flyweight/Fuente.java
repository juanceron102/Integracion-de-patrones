package motor.flyweight;


public final class Fuente {
    private final String familia;
    private final boolean negrita;

    Fuente(String familia, boolean negrita) {
        this.familia = familia;
        this.negrita = negrita;
    }

    public String getFamilia() {
        return familia;
    }

    public boolean isNegrita() {
        return negrita;
    }

    @Override
    public String toString() {
        return familia + (negrita ? "-Bold" : "");
    }
}
