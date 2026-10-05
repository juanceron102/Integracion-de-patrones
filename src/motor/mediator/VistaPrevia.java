package motor.mediator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class VistaPrevia extends ComponenteEditor {
    private String ultimoResultado;

    public VistaPrevia(DocumentEditorMediator mediator) {
        super(mediator);
    }

    public void mostrarEstado(String estado) {
        log("Estado: " + estado);
    }

    public void mostrarRegistro(List<String> mensajes) {
        mensajes.forEach(m -> System.out.println("    " + m));
    }

    public void mostrarError(String error) {
        log("ERROR CRITICO, exportacion cancelada: " + error);
    }

    public void mostrar(String formato, String contenido) {
        ultimoResultado = contenido;
        System.out.println("  +------------------- Vista previa " + formato + " -------------------");
        contenido.lines().forEach(l -> System.out.println("  | " + l));
        System.out.println("  +----------------------------------------------------------------");
    }

    public void mostrarArchivo(Path archivo) throws IOException {
        log("Archivo generado: " + archivo.toAbsolutePath() + " (" + Files.size(archivo) + " bytes)");
    }

    public String getUltimoResultado() {
        return ultimoResultado;
    }
}
