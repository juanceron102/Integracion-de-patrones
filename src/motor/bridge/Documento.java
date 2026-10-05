package motor.bridge;

import java.util.ArrayList;
import java.util.List;

import motor.builder.*;
import motor.flyweight.*;

public abstract class Documento {
    protected static final double ANCHO_PAGINA = 595;
    protected static final double MARGEN = 40;
    protected static final double ALTO_LINEA = 14;
    protected static final double ANCHO_CARACTER = 7;

    protected RenderizadorEngine engine;
    protected final ContenidoDocumento contenido;
    protected final GlifoFactory glifos;
    protected double cursorY;

    protected Documento(ContenidoDocumento contenido, GlifoFactory glifos, RenderizadorEngine engine) {
        this.contenido = contenido;
        this.glifos = glifos;
        this.engine = engine;
    }

    public void setEngine(RenderizadorEngine engine) {
        this.engine = engine;
    }

    public final String renderizar() {
        engine.iniciar(contenido.getTitulo());
        comenzar();
        for (ElementoDocumento elemento : contenido.getElementos()) {
            reservarEspacio(altoEstimado(elemento));
            renderizarElemento(elemento);
        }
        terminar();
        return engine.finalizar();
    }

    protected abstract void comenzar();

    protected abstract void reservarEspacio(double alto);

    protected abstract void terminar();

    private void renderizarElemento(ElementoDocumento elemento) {
        double escala = elemento.getTipo() == TipoElemento.ENCABEZADO ? 1.6 : 1.0;
        String color = colorPara(elemento.getTipo());
        double x = MARGEN;
        if (elemento.getIcono() != null) {
            engine.dibujarIcono(new GlifoPosicionado(elemento.getIcono(),
                    new EstadoExtrinseco(x, cursorY, color, escala)));
            x += 3 * ANCHO_CARACTER * escala;
        }
        if (elemento instanceof Tabla tabla) {
            if (elemento.getIcono() != null) {
                cursorY += ALTO_LINEA * 1.5; 
            }
            engine.dibujarTabla(tabla.getCabecera(), tabla.getFilas(), MARGEN, cursorY);
            cursorY += (tabla.getFilas().size() + 2) * ALTO_LINEA;
        } else {
            engine.dibujarTexto(elemento.getTipo(), maquetar(elemento, x, color, escala));
        }
    }

    private List<GlifoPosicionado> maquetar(ElementoDocumento elemento, double xInicial, String color, double escala) {
        List<GlifoPosicionado> resultado = new ArrayList<>();
        double x = xInicial;
        for (char c : elemento.getTexto().toCharArray()) {
            if (x + ANCHO_CARACTER * escala > ANCHO_PAGINA - MARGEN) {
                x = xInicial;
                cursorY += ALTO_LINEA * escala;
            }
            resultado.add(new GlifoPosicionado(glifos.getCaracter(c, elemento.getFuente()),
                    new EstadoExtrinseco(x, cursorY, color, escala)));
            x += ANCHO_CARACTER * escala;
        }
        cursorY += ALTO_LINEA * escala * 1.5;
        return resultado;
    }

    private double altoEstimado(ElementoDocumento elemento) {
        if (elemento instanceof Tabla tabla) {
            return (tabla.getFilas().size() + 2) * ALTO_LINEA + (tabla.getIcono() != null ? ALTO_LINEA * 1.5 : 0);
        }
        double escala = elemento.getTipo() == TipoElemento.ENCABEZADO ? 1.6 : 1.0;
        double anchoUtil = ANCHO_PAGINA - 2 * MARGEN;
        double lineas = Math.ceil(elemento.getTexto().length() * ANCHO_CARACTER * escala / anchoUtil);
        return Math.max(1, lineas) * ALTO_LINEA * escala * 1.5;
    }

    private static String colorPara(TipoElemento tipo) {
        return switch (tipo) {
            case ENCABEZADO -> "#1F3A93";
            case PIE -> "#777777";
            default -> "#000000";
        };
    }
}
