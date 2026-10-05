package motor.bridge;

import java.nio.charset.*;
import java.util.*;

import motor.builder.TipoElemento;
import motor.flyweight.*;


public class PdfRenderEngine implements RenderizadorEngine {
    private static final double ANCHO_PAGINA = 595; // A4 en puntos
    private static final double ALTO_PAGINA = 842;
    private static final double MARGEN = 40;
    private static final double ALTO_FILA = 14;
    private static final double TAMANO_TABLA = 10;

   
    private static final Map<String, String> FUENTES = new LinkedHashMap<>();
    static {
        FUENTES.put("F1", "Helvetica");
        FUENTES.put("F2", "Helvetica-Bold");
        FUENTES.put("F3", "Times-Roman");
        FUENTES.put("F4", "Times-Bold");
        FUENTES.put("F5", "Courier");
        FUENTES.put("F6", "Courier-Bold");
    }
    private String titulo;
    private List<Pagina> paginas;
    private Pagina actual;

    private static final class Pagina {
        private final StringBuilder operaciones = new StringBuilder();
        private double yMaxima;

        double alto() {
            return Math.max(ALTO_PAGINA, yMaxima + MARGEN);
        }

        String contenido() {
            return String.format(Locale.ROOT, "1 0 0 1 0 %.2f cm%n", alto()) + operaciones;
        }
    }

    @Override
    public String getFormato() {
        return "PDF";
    }

    @Override
    public String getExtension() {
        return "pdf";
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.ISO_8859_1;
    }

    @Override
    public boolean isBinario() {
        return true;
    }

    @Override
    public void iniciar(String titulo) {
        this.titulo = titulo;
        this.paginas = new ArrayList<>();
        this.actual = null;
    }

    @Override
    public void iniciarPagina(int numero) {
        actual = new Pagina();
        paginas.add(actual);
    }

    @Override
    public void finalizarPagina(int numero) {
        actual = null;
    }

    private Pagina pagina() {
        if (actual == null) {
            iniciarPagina(paginas.size() + 1);
        }
        return actual;
    }

    @Override
    public void dibujarIcono(GlifoPosicionado icono) {
        EstadoExtrinseco e = icono.estado();
        escribirTexto("F2", 12 * e.escala(), e.color(), e.x(), e.y(), icono.glifo().getSimbolo());
    }

    @Override
    public void dibujarTexto(TipoElemento tipo, List<GlifoPosicionado> glifos) {
        for (List<GlifoPosicionado> linea : RenderizadorEngine.lineas(glifos)) {
            GlifoPosicionado primero = linea.get(0);
            Fuente fuente = primero.glifo() instanceof Caracter c ? c.getFuente() : null;
            EstadoExtrinseco e = primero.estado();
            escribirTexto(recursoFuente(fuente), 12 * e.escala(), e.color(), e.x(), e.y(),
                    RenderizadorEngine.texto(linea));
        }
    }

    @Override
    public void dibujarTabla(List<String> cabecera, List<List<String>> filas, double x, double y) {
        Pagina p = pagina();
        double ancho = ANCHO_PAGINA - x - MARGEN;
        double anchoColumna = ancho / cabecera.size();
        double alto = (filas.size() + 1) * ALTO_FILA;

        p.operaciones.append(String.format(Locale.ROOT, "0.9 0.9 0.9 rg %.2f %.2f %.2f %.2f re f%n",
                x, -(y + ALTO_FILA), ancho, ALTO_FILA));

        StringBuilder bordes = new StringBuilder("0.5 w 0 0 0 RG\n");
        bordes.append(String.format(Locale.ROOT, "%.2f %.2f %.2f %.2f re S%n", x, -(y + alto), ancho, alto));
        for (int i = 1; i <= filas.size(); i++) {
            double yi = -(y + i * ALTO_FILA);
            bordes.append(String.format(Locale.ROOT, "%.2f %.2f m %.2f %.2f l S%n", x, yi, x + ancho, yi));
        }
        for (int j = 1; j < cabecera.size(); j++) {
            double xj = x + j * anchoColumna;
            bordes.append(String.format(Locale.ROOT, "%.2f %.2f m %.2f %.2f l S%n", xj, -y, xj, -(y + alto)));
        }
        p.operaciones.append(bordes);

        for (int j = 0; j < cabecera.size(); j++) {
            escribirTexto("F2", TAMANO_TABLA, "#000000", x + j * anchoColumna + 4, y + 2, cabecera.get(j));
        }
        for (int i = 0; i < filas.size(); i++) {
            List<String> fila = filas.get(i);
            for (int j = 0; j < fila.size(); j++) {
                escribirTexto("F1", TAMANO_TABLA, "#000000", x + j * anchoColumna + 4,
                        y + (i + 1) * ALTO_FILA + 2, fila.get(j));
            }
        }
        p.yMaxima = Math.max(p.yMaxima, y + alto);
    }

    private void escribirTexto(String recurso, double tamano, String color, double x, double y, String texto) {
        Pagina p = pagina();
        double lineaBase = y + tamano * 0.8;
        p.operaciones.append(String.format(Locale.ROOT, "BT /%s %.1f Tf %s rg %.2f %.2f Td (%s) Tj ET%n",
                recurso, tamano, colorRgb(color), x, -lineaBase, escapar(texto)));
        p.yMaxima = Math.max(p.yMaxima, y + tamano);
    }

    @Override
    public String finalizar() {
        if (paginas.isEmpty()) {
            pagina();
        }
        List<String> objetos = new ArrayList<>(); // el objeto N está en la posición N-1
        objetos.add("<< /Type /Catalog /Pages 2 0 R >>");
        objetos.add(null); // /Pages, se completa cuando se conocen las páginas

        StringBuilder recursos = new StringBuilder("<< /Font << ");
        for (Map.Entry<String, String> f : FUENTES.entrySet()) {
            objetos.add("<< /Type /Font /Subtype /Type1 /BaseFont /" + f.getValue()
                    + " /Encoding /WinAnsiEncoding >>");
            recursos.append('/').append(f.getKey()).append(' ').append(objetos.size()).append(" 0 R ");
        }
        recursos.append(">> >>");

        List<Integer> kids = new ArrayList<>();
        for (Pagina p : paginas) {
            String contenido = p.contenido();
            int numeroPagina = objetos.size() + 1;
            kids.add(numeroPagina);
            objetos.add(String.format(Locale.ROOT,
                    "<< /Type /Page /Parent 2 0 R /MediaBox [0 0 %.0f %.2f] /Resources %s /Contents %d 0 R >>",
                    ANCHO_PAGINA, p.alto(), recursos, numeroPagina + 1));
            objetos.add("<< /Length " + contenido.length() + " >>\nstream\n" + contenido + "\nendstream");
        }
        StringBuilder refs = new StringBuilder();
        kids.forEach(k -> refs.append(k).append(" 0 R "));
        objetos.set(1, "<< /Type /Pages /Kids [" + refs.toString().trim() + "] /Count " + kids.size() + " >>");
        objetos.add("<< /Title (" + escapar(titulo) + ") /Producer (Motor de Documentos Inteligentes) >>");
        int info = objetos.size();

        StringBuilder pdf = new StringBuilder("%PDF-1.4\n%âãÏÓ\n");
        List<Integer> posiciones = new ArrayList<>();
        for (int i = 0; i < objetos.size(); i++) {
            posiciones.add(pdf.length());
            pdf.append(i + 1).append(" 0 obj\n").append(objetos.get(i)).append("\nendobj\n");
        }
        int inicioXref = pdf.length();
        pdf.append("xref\n0 ").append(objetos.size() + 1).append('\n');
        pdf.append("0000000000 65535 f \n");
        posiciones.forEach(pos -> pdf.append(String.format("%010d 00000 n \n", pos)));
        pdf.append("trailer\n<< /Size ").append(objetos.size() + 1)
                .append(" /Root 1 0 R /Info ").append(info).append(" 0 R >>\n")
                .append("startxref\n").append(inicioXref).append("\n%%EOF\n");
        return pdf.toString();
    }

    private static String recursoFuente(Fuente fuente) {
        if (fuente == null) {
            return "F1";
        }
        int base = switch (fuente.getFamilia().toLowerCase()) {
            case "serif" -> 3;
            case "monospace" -> 5;
            default -> 1;
        };
        return "F" + (base + (fuente.isNegrita() ? 1 : 0));
    }

    private static String colorRgb(String hex) {
        int rgb = Integer.parseInt(hex.substring(1), 16);
        return String.format(Locale.ROOT, "%.3f %.3f %.3f",
                (rgb >> 16 & 0xFF) / 255.0, (rgb >> 8 & 0xFF) / 255.0, (rgb & 0xFF) / 255.0);
    }

    private static String escapar(String texto) {
        StringBuilder sb = new StringBuilder();
        for (char c : texto.toCharArray()) {
            switch (c) {
                case '\\', '(', ')' -> sb.append('\\').append(c);
                default -> sb.append(c <= 0xFF ? c : '?');
            }
        }
        return sb.toString();
    }
}
