package motor.builder;

import java.util.List;
import java.util.function.UnaryOperator;

import motor.flyweight.*;

public class ElementoDocumento {
    private final TipoElemento tipo;
    private String texto;
    private final Fuente fuente;
    private final Icono icono; // flyweight compartido, puede ser null

    public ElementoDocumento(TipoElemento tipo, String texto, Fuente fuente, Icono icono) {
        this.tipo = tipo;
        this.texto = texto;
        this.fuente = fuente;
        this.icono = icono;
    }

    public TipoElemento getTipo() {
        return tipo;
    }

    public String getTexto() {
        return texto;
    }

    public Fuente getFuente() {
        return fuente;
    }

    public Icono getIcono() {
        return icono;
    }

    public void transformarTextos(UnaryOperator<String> operacion) {
        texto = operacion.apply(texto);
    }

    public List<String> getTextos() {
        return List.of(texto);
    }

    public ElementoDocumento copiar(boolean conIcono) {
        return new ElementoDocumento(tipo, texto, fuente, conIcono ? icono : null);
    }
}
