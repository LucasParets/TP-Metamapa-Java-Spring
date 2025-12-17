package ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.estadisticas;

public interface CantSpamMensual {
    Integer getAnio();
    Integer getMes();
    Long getTotalSpam();
}
