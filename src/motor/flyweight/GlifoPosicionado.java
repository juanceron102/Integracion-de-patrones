package motor.flyweight;

public record GlifoPosicionado(Glifo glifo, EstadoExtrinseco estado) {

    public String dibujar() {
        return glifo.dibujar(estado);
    }
}
