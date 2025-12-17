package ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.estadisticas;

public interface ProvinciaTopCategoriaMensual {
    Integer getAnio();
    Integer getMes();
    String getProvinciaNombre();
    Long getTotalHechos();
}
