package motor.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;


public class ContenidoDocumento {
    private final String titulo;
    private final String plantilla;
    private final List<ElementoDocumento> elementos;

    public ContenidoDocumento(String titulo, String plantilla, List<ElementoDocumento> elementos) {
        this.titulo = titulo;
        this.plantilla = plantilla;
        this.elementos = new ArrayList<>(elementos);
    }

    public String getTitulo() {
        return titulo;
    }

    public String getPlantilla() {
        return plantilla;
    }

    public List<ElementoDocumento> getElementos() {
        return elementos;
    }

    public void transformarTextos(UnaryOperator<String> operacion) {
        elementos.forEach(e -> e.transformarTextos(operacion));
    }

    public List<String> getTextos() {
        List<String> textos = new ArrayList<>();
        elementos.forEach(e -> textos.addAll(e.getTextos()));
        return textos;
    }
}
