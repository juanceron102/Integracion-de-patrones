package motor.bridge;

import java.util.List;

import motor.builder.TipoElemento;
import motor.flyweight.GlifoPosicionado;

public class MarkdownRenderEngine implements RenderizadorEngine {
    private StringBuilder salida;

    @Override
    public String getFormato() {
        return "MARKDOWN";
    }

    @Override
    public String getExtension() {
        return "md";
    }

    @Override
    public void iniciar(String titulo) {
        salida = new StringBuilder();
    }

    @Override
    public void iniciarPagina(int numero) {
        salida.append("<!-- pagina ").append(numero).append(" -->\n\n");
    }

    @Override
    public void finalizarPagina(int numero) {
        salida.append("\n");
    }

    @Override
    public void dibujarIcono(GlifoPosicionado icono) {
        salida.append(icono.glifo().getSimbolo()).append(' ');
    }

    @Override
    public void dibujarTexto(TipoElemento tipo, List<GlifoPosicionado> glifos) {
        String texto = RenderizadorEngine.texto(glifos);
        switch (tipo) {
            case ENCABEZADO -> salida.append("# ").append(texto).append("\n\n");
            case PIE -> salida.append("---\n_").append(texto).append("_\n");
            default -> salida.append(texto).append("\n\n");
        }
    }

    @Override
    public void dibujarTabla(List<String> cabecera, List<List<String>> filas, double x, double y) {
        salida.append("| ").append(String.join(" | ", cabecera)).append(" |\n|");
        cabecera.forEach(c -> salida.append("---|"));
        salida.append('\n');
        filas.forEach(f -> salida.append("| ").append(String.join(" | ", f)).append(" |\n"));
        salida.append('\n');
    }

    @Override
    public String finalizar() {
        return salida.toString().stripTrailing();
    }
}
