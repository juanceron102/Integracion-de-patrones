package motor.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import motor.flyweight.Fuente;
import motor.flyweight.Icono;

public class Tabla extends ElementoDocumento {
    private final List<String> cabecera;
    private final List<List<String>> filas;

    public Tabla(List<String> cabecera, List<List<String>> filas, Fuente fuente, Icono icono) {
        super(TipoElemento.TABLA, "", fuente, icono);
        this.cabecera = new ArrayList<>(cabecera);
        this.filas = new ArrayList<>();
        filas.forEach(f -> this.filas.add(new ArrayList<>(f)));
    }

    public List<String> getCabecera() {
        return cabecera;
    }

    public List<List<String>> getFilas() {
        return filas;
    }

    @Override
    public void transformarTextos(UnaryOperator<String> operacion) {
        cabecera.replaceAll(operacion);
        filas.forEach(f -> f.replaceAll(operacion));
    }

    @Override
    public List<String> getTextos() {
        List<String> textos = new ArrayList<>(cabecera);
        filas.forEach(textos::addAll);
        return textos;
    }

    @Override
    public ElementoDocumento copiar(boolean conIcono) {
        return new Tabla(cabecera, filas, getFuente(), conIcono ? getIcono() : null);
    }
}
