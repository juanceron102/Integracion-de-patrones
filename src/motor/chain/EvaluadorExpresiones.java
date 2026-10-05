package motor.chain;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import motor.builder.ContenidoDocumento;
import motor.interpreter.*;

public class EvaluadorExpresiones extends ProcesadorHandler {
    private static final Pattern MARCADOR = Pattern.compile("#\\{([^}]*)}");
    private final Contexto contexto;
    private final ParserExpresiones parser = new ParserExpresiones();

    public EvaluadorExpresiones(Contexto contexto) {
        this.contexto = contexto;
    }

    @Override
    protected void manejar(ContenidoDocumento documento, RegistroProcesamiento registro)
            throws ErrorCriticoException {
        int[] evaluadas = {0};
        try {
            documento.transformarTextos(texto -> {
                Matcher m = MARCADOR.matcher(texto);
                return m.replaceAll(r -> {
                    String formula = r.group(1).trim();
                    Expresion expresion = parser.parsear(formula);
                    String valor = formatear(expresion.interpretar(contexto));
                    registro.agregar(nombre(), formula + " = " + valor);
                    evaluadas[0]++;
                    return Matcher.quoteReplacement(valor);
                });
            });
        } catch (IllegalArgumentException | ArithmeticException e) {
            throw new ErrorCriticoException("Fórmula inválida: " + e.getMessage());
        }
        registro.agregar(nombre(), evaluadas[0] + " expresion(es) evaluada(s)");
    }

    private static String formatear(Object valor) {
        if (valor instanceof Number n) {
            double d = n.doubleValue();
            return d == Math.floor(d) ? String.format(Locale.ROOT, "%,.0f", d) : String.format(Locale.ROOT, "%,.2f", d);
        }
        return String.valueOf(valor);
    }
}
