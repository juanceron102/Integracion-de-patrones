package motor.chain;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import motor.builder.ContenidoDocumento;

public class ValidadorSintaxis extends ProcesadorHandler {
    private static final Pattern ETIQUETA = Pattern.compile("<(/?)([a-zA-Z]+)>");

    @Override
    protected void manejar(ContenidoDocumento documento, RegistroProcesamiento registro)
            throws ErrorCriticoException {
        for (String texto : documento.getTextos()) {
            validarMarcadores(texto);
            validarEtiquetas(texto);
        }
        registro.agregar(nombre(), "Sintaxis correcta");
    }

    private void validarMarcadores(String texto) throws ErrorCriticoException {
        int desde = 0;
        while ((desde = texto.indexOf("#{", desde)) >= 0) {
            int cierre = texto.indexOf('}', desde);
            int siguienteApertura = texto.indexOf("#{", desde + 2);
            if (cierre < 0 || (siguienteApertura >= 0 && siguienteApertura < cierre)) {
                throw new ErrorCriticoException("Marcador sin cerrar en: \"" + texto + "\"");
            }
            desde = cierre + 1;
        }
    }

    private void validarEtiquetas(String texto) throws ErrorCriticoException {
        Deque<String> abiertas = new ArrayDeque<>();
        Matcher m = ETIQUETA.matcher(texto);
        while (m.find()) {
            String etiqueta = m.group(2);
            if (m.group(1).isEmpty()) {
                abiertas.push(etiqueta);
            } else if (abiertas.isEmpty() || !abiertas.pop().equals(etiqueta)) {
                throw new ErrorCriticoException("Etiqueta </" + etiqueta + "> corrupta en: \"" + texto + "\"");
            }
        }
        if (!abiertas.isEmpty()) {
            throw new ErrorCriticoException("Etiqueta <" + abiertas.peek() + "> sin cerrar en: \"" + texto + "\"");
        }
        String sinEtiquetas = ETIQUETA.matcher(texto).replaceAll("");
        if (sinEtiquetas.contains("<") || sinEtiquetas.contains(">")) {
            throw new ErrorCriticoException("Etiqueta mal formada en: \"" + texto + "\"");
        }
    }
}
