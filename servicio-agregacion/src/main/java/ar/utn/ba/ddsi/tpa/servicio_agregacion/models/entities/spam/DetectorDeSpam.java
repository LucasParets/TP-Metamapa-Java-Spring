package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.spam;

public interface DetectorDeSpam {
    public boolean esSpam(String texto);
}
