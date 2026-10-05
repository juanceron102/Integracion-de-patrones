package motor.interpreter;

public class ParserExpresiones {
    private String fuente;
    private int pos;

    public Expresion parsear(String texto) {
        fuente = texto;
        pos = 0;
        Expresion resultado = expresion();
        saltarEspacios();
        if (pos < fuente.length()) {
            throw new IllegalArgumentException("Símbolo inesperado '" + fuente.charAt(pos) + "' en: " + texto);
        }
        return resultado;
    }

    private Expresion expresion() {
        Expresion izquierda = termino();
        while (true) {
            if (consumir('+')) {
                izquierda = new Suma(izquierda, termino());
            } else if (consumir('-')) {
                izquierda = new Resta(izquierda, termino());
            } else {
                return izquierda;
            }
        }
    }

    private Expresion termino() {
        Expresion izquierda = factor();
        while (true) {
            if (consumir('*')) {
                izquierda = new Multiplicacion(izquierda, factor());
            } else if (consumir('/')) {
                izquierda = new Division(izquierda, factor());
            } else {
                return izquierda;
            }
        }
    }

    private Expresion factor() {
        saltarEspacios();
        if (consumir('(')) {
            Expresion interna = expresion();
            if (!consumir(')')) {
                throw new IllegalArgumentException("Falta ')' en: " + fuente);
            }
            return interna;
        }
        int inicio = pos;
        if (pos < fuente.length() && (Character.isDigit(fuente.charAt(pos)) || fuente.charAt(pos) == '.')) {
            while (pos < fuente.length() && (Character.isDigit(fuente.charAt(pos)) || fuente.charAt(pos) == '.')) {
                pos++;
            }
            return new Numero(Double.parseDouble(fuente.substring(inicio, pos)));
        }
        if (pos < fuente.length() && (Character.isLetter(fuente.charAt(pos)) || fuente.charAt(pos) == '_')) {
            while (pos < fuente.length() && (Character.isLetterOrDigit(fuente.charAt(pos)) || fuente.charAt(pos) == '_')) {
                pos++;
            }
            return new Variable(fuente.substring(inicio, pos));
        }
        throw new IllegalArgumentException("Se esperaba un número, variable o '(' en: " + fuente);
    }

    private boolean consumir(char esperado) {
        saltarEspacios();
        if (pos < fuente.length() && fuente.charAt(pos) == esperado) {
            pos++;
            return true;
        }
        return false;
    }

    private void saltarEspacios() {
        while (pos < fuente.length() && Character.isWhitespace(fuente.charAt(pos))) {
            pos++;
        }
    }
}
