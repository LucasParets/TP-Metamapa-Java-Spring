package ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.estadisticas;

public interface ProvinciaTopPorColeccion {
    String getColeccionHandle();
    String getColeccionTitulo();
    String getProvinciaNombre();
    Long getTotalHechos();
}
