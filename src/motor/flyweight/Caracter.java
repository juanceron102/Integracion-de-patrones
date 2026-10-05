package motor.flyweight;

import java.util.Locale;

public final class Caracter implements Glifo {
    private final char simbolo;
    private final Fuente fuente;

    Caracter(char simbolo, Fuente fuente) {
        this.simbolo = simbolo;
        this.fuente = fuente;
    }

    public Fuente getFuente() {
        return fuente;
    }

    @Override
    public String getSimbolo() {
        return String.valueOf(simbolo);
    }

    @Override
    public String dibujar(EstadoExtrinseco e) {
        return String.format(Locale.ROOT, "'%c' %s @(%.0f,%.0f) %s x%.1f", simbolo, fuente, e.x(), e.y(), e.color(), e.escala());
    }
}
