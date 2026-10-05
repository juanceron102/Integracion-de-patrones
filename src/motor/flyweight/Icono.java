package motor.flyweight;

import java.util.Locale;

public final class Icono implements Glifo {
    private final String nombre;
    private final String simbolo;
    private final byte[] imagen;

    Icono(String nombre, String simbolo) {
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.imagen = new byte[4096]; 
    }

    public String getNombre() {
        return nombre;
    }

    public int getTamanoImagen() {
        return imagen.length;
    }

    @Override
    public String getSimbolo() {
        return simbolo;
    }

    @Override
    public String dibujar(EstadoExtrinseco e) {
        return String.format(Locale.ROOT, "[icono %s] @(%.0f,%.0f) %s x%.1f", nombre, e.x(), e.y(), e.color(), e.escala());
    }
}
