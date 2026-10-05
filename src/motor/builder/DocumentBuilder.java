package motor.builder;

import java.util.List;

public interface DocumentBuilder {

    DocumentBuilder reset();

    DocumentBuilder addHeader(String texto);

    DocumentBuilder addParagraph(String texto);

    DocumentBuilder addTable(List<String> cabecera, List<List<String>> filas);

    DocumentBuilder addFooter(String texto);

    void setIncluirIconos(boolean incluirIconos);

    String getNombrePlantilla();

    ContenidoDocumento build();
}
