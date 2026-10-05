package motor.bridge;

import java.util.List;
import java.util.Locale;

import motor.builder.TipoElemento;
import motor.flyweight.*;

public class HtmlRenderEngine implements RenderizadorEngine {
    private StringBuilder salida;

    @Override
    public String getFormato() {
        return "HTML";
    }

    @Override
    public String getExtension() {
        return "html";
    }

    @Override
    public void iniciar(String titulo) {
        salida = new StringBuilder("<!DOCTYPE html>\n<html>\n<head><meta charset=\"UTF-8\"><title>")
                .append(escapar(titulo)).append("</title></head>\n<body>\n");
    }

    @Override
    public void iniciarPagina(int numero) {
        salida.append("<section class=\"pagina\" data-numero=\"").append(numero).append("\">\n");
    }

    @Override
    public void finalizarPagina(int numero) {
        salida.append("</section>\n");
    }

    @Override
    public void dibujarIcono(GlifoPosicionado icono) {
        String nombre = icono.glifo() instanceof Icono i ? i.getNombre() : "";
        salida.append("  <span class=\"icono icono-").append(nombre).append("\">")
                .append(escapar(icono.glifo().getSimbolo())).append("</span>\n");
    }

    @Override
    public void dibujarTexto(TipoElemento tipo, List<GlifoPosicionado> glifos) {
        if (glifos.isEmpty()) {
            return;
        }
        String etiqueta = switch (tipo) {
            case ENCABEZADO -> "h1";
            case PIE -> "footer";
            default -> "p";
        };
        EstadoExtrinseco e = glifos.get(0).estado();
        String fuente = glifos.get(0).glifo() instanceof Caracter c ? c.getFuente().getFamilia() : "inherit";
        salida.append(String.format(Locale.ROOT, "  <%s style=\"color:%s;font-family:%s;font-size:%.1fpx\">%s</%s>%n",
                etiqueta, e.color(), fuente, 12 * e.escala(), escapar(RenderizadorEngine.texto(glifos)), etiqueta));
    }

    @Override
    public void dibujarTabla(List<String> cabecera, List<List<String>> filas, double x, double y) {
        salida.append("  <table>\n    <tr>");
        cabecera.forEach(c -> salida.append("<th>").append(escapar(c)).append("</th>"));
        salida.append("</tr>\n");
        for (List<String> fila : filas) {
            salida.append("    <tr>");
            fila.forEach(c -> salida.append("<td>").append(escapar(c)).append("</td>"));
            salida.append("</tr>\n");
        }
        salida.append("  </table>\n");
    }

    @Override
    public String finalizar() {
        return salida.append("</body>\n</html>").toString();
    }

    private static String escapar(String texto) {
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
