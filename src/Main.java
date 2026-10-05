import java.util.List;
import java.util.Set;

import motor.chain.*;
import motor.flyweight.GlifoFactory;
import motor.interpreter.Contexto;
import motor.mediator.*;

public class Main {

    public static void main(String[] args) {
        GlifoFactory glifos = new GlifoFactory();

        Contexto contexto = new Contexto();
        contexto.definir("CLIENTE", "ACME S.A.S.");
        contexto.definir("PRECIO_BASE", 1500000);
        contexto.definir("DESCUENTO", 50000);
        contexto.definir("INGRESOS", 820000000);
        contexto.definir("COSTOS", 615000000);

        ProcesadorHandler cadena = new ValidadorSintaxis();
        cadena.enlazar(new Sanitizador(Set.of("confidencial", "contrasena"))).enlazar(new EvaluadorExpresiones(contexto));

        EditorDocumentosMediator mediator = new EditorDocumentosMediator(glifos, cadena);
        SelectorDeFormato selector = new SelectorDeFormato(mediator);
        BarraDeHerramientasBuilder barra = new BarraDeHerramientasBuilder(mediator);
        VistaPrevia vista = new VistaPrevia(mediator);
        BotonExportar boton = new BotonExportar(mediator);
        mediator.registrar(selector, barra, vista, boton);

        titulo("1. Configuracion via Mediator");
        boton.click();
        barra.seleccionarPlantilla("FACTURA");
        selector.seleccionarFormato("PDF");
        selector.seleccionarDisposicion("PAGINADO");

        titulo("2. Construccion de la factura via Builder (+ Flyweights)");
        barra.agregarEncabezado("Factura electrónica No. 001");
        barra.agregarParrafo("Cliente: #{CLIENTE} - Fecha de emisión: #{FECHA_ACTUAL}");
        barra.agregarParrafo("Pago con tarjeta 4111 1111 1111 1234. Documento confidencial.");
        barra.agregarTabla(List.of("Concepto", "Valor"), List.of(
                List.of("Precio base", "#{PRECIO_BASE}"),
                List.of("IVA 19%", "#{PRECIO_BASE * 0.19}"),
                List.of("Descuento", "#{DESCUENTO}"),
                List.of("Total", "#{PRECIO_BASE * 1.19 - DESCUENTO}")));
        barra.agregarPie("Generado el #{FECHA_ACTUAL}");

        titulo("3. Exportar factura: Chain + Interpreter + Bridge (PDF paginado)");
        boton.click();

        titulo("4. Mismo documento, otro formato: HTML continuo");
        selector.seleccionarFormato("HTML");
        selector.seleccionarDisposicion("CONTINUO");
        boton.click();

        titulo("5. Reporte ejecutivo en Markdown");
        barra.seleccionarPlantilla("REPORTE");
        selector.seleccionarFormato("MARKDOWN");
        selector.seleccionarDisposicion("PAGINADO");
        barra.agregarEncabezado("Reporte financiero Q3");
        barra.agregarParrafo("Margen operativo: #{(INGRESOS - COSTOS) / INGRESOS * 100}%");
        barra.agregarParrafo("La contrasena del portal no debe aparecer en el reporte.");
        barra.agregarTabla(List.of("Indicador", "Valor"), List.of(
                List.of("Ingresos", "#{INGRESOS}"),
                List.of("Costos", "#{COSTOS}"),
                List.of("Utilidad", "#{INGRESOS - COSTOS}")));
        barra.agregarPie("#{FECHA_ACTUAL}");
        boton.click();
        selector.seleccionarFormato("PDF");
        boton.click();

        titulo("6. Documento corrupto: la cadena se interrumpe");
        barra.agregarParrafo("Proyeccion: #{INGRESOS * (1.10 ");
        boton.click();

        titulo("7. Uso de memoria (Flyweight)");
        System.out.println("  " + glifos.estadisticas());
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }
}
