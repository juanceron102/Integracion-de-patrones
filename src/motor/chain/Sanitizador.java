package motor.chain;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import motor.builder.ContenidoDocumento;

public class Sanitizador extends ProcesadorHandler {
    private static final Pattern TARJETA = Pattern.compile("\\b(?:\\d{4}[ -]?){3}(\\d{4})\\b");
    private final Pattern prohibidas;

    public Sanitizador(Set<String> palabrasProhibidas) {
        this.prohibidas = Pattern.compile("\\b(" + String.join("|", palabrasProhibidas) + ")\\b",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    @Override
    protected void manejar(ContenidoDocumento documento, RegistroProcesamiento registro) {
        AtomicInteger ocultos = new AtomicInteger();
        documento.transformarTextos(texto -> {
            Matcher tarjeta = TARJETA.matcher(texto);
            String limpio = tarjeta.replaceAll(r -> {
                ocultos.incrementAndGet();
                return "****-****-****-" + r.group(1);
            });
            return prohibidas.matcher(limpio).replaceAll(r -> {
                ocultos.incrementAndGet();
                return "*".repeat(r.group().length());
            });
        });
        registro.agregar(nombre(), ocultos.get() + " dato(s) sensible(s) ocultado(s)");
    }
}
