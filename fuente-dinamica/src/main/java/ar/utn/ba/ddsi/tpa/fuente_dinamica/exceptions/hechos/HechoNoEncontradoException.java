package ar.utn.ba.ddsi.tpa.fuente_dinamica.exceptions.hechos;

public class HechoNoEncontradoException extends RuntimeException{
    public HechoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
