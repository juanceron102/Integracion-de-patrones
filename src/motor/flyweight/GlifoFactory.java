package motor.flyweight;

import java.util.HashMap;
import java.util.Map;


public class GlifoFactory {
    private final Map<String, Fuente> fuentes = new HashMap<>();
    private final Map<String, Caracter> caracteres = new HashMap<>();
    private final Map<String, Icono> iconos = new HashMap<>();
    private long solicitudes;

    public Fuente getFuente(String familia, boolean negrita) {
        return fuentes.computeIfAbsent(familia + "|" + negrita, k -> new Fuente(familia, negrita));
    }

    public Caracter getCaracter(char simbolo, Fuente fuente) {
        solicitudes++;
        return caracteres.computeIfAbsent(fuente + "|" + simbolo, k -> new Caracter(simbolo, fuente));
    }

    public Icono getIcono(String nombre) {
        solicitudes++;
        return iconos.computeIfAbsent(nombre, n -> new Icono(n, simboloPara(n)));
    }

    private static String simboloPara(String nombre) {
        return switch (nombre) {
            case "logo" -> "[#]";
            case "vineta" -> "*";
            case "factura" -> "[$]";
            case "grafico" -> "[~]";
            case "check" -> "[v]";
            default -> "[?]";
        };
    }

    public int getTotalInstancias() {
        return fuentes.size() + caracteres.size() + iconos.size();
    }

    public String estadisticas() {
        return String.format(
                "Solicitudes de glifos: %d | Instancias compartidas: %d (fuentes=%d, caracteres=%d, iconos=%d)",
                solicitudes, getTotalInstancias(), fuentes.size(), caracteres.size(), iconos.size());
    }
}
