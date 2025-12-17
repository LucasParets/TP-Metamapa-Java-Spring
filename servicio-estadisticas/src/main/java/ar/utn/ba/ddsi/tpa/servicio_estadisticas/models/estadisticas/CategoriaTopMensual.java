package ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.estadisticas;

public interface CategoriaTopMensual {
    Integer getAnio();
    Integer getMes();
    String getCategoriaNombre();
    Long getTotalHechos();
}
