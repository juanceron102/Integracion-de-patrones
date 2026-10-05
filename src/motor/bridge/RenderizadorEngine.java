package motor.bridge;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import motor.builder.TipoElemento;
import motor.flyweight.GlifoPosicionado;

public interface RenderizadorEngine {

    String getFormato();

    String getExtension();

    default Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    default boolean isBinario() {
        return false;
    }

    void iniciar(String titulo);

    void iniciarPagina(int numero);

    void finalizarPagina(int numero);

    void dibujarIcono(GlifoPosicionado icono);

    void dibujarTexto(TipoElemento tipo, List<GlifoPosicionado> glifos);

    void dibujarTabla(List<String> cabecera, List<List<String>> filas, double x, double y);

    String finalizar();

    static String texto(List<GlifoPosicionado> glifos) {
        StringBuilder sb = new StringBuilder();
        glifos.forEach(g -> sb.append(g.glifo().getSimbolo()));
        return sb.toString();
    }

    static List<List<GlifoPosicionado>> lineas(List<GlifoPosicionado> glifos) {
        List<List<GlifoPosicionado>> lineas = new ArrayList<>();
        double yActual = Double.NaN;
        for (GlifoPosicionado g : glifos) {
            if (g.estado().y() != yActual) {
                lineas.add(new ArrayList<>());
                yActual = g.estado().y();
            }
            lineas.get(lineas.size() - 1).add(g);
        }
        return lineas;
    }
}
