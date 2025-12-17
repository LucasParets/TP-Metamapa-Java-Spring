package ar.utn.ba.ddsi.tpa.servicio_estadisticas.services;

import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.Categoria;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.Provincia;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.estadisticas.*;

import java.time.LocalDateTime;
import java.util.List;

public interface IEstadisticasService {
    List<ProvinciaTopPorColeccion> getProvinciaConMasHechosEnUnaColeccion(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<CategoriaTopMensual> getCategoriaConMasHechos();
    List<ProvinciaTopCategoriaMensual> getProvinciaConMasHechosEnUnaCategoria(Long id_categoria);
    List<HoraPicoMensualCategoria> getHoraConMasHechosEnUnaCategoria(Long id_categoria);
    List<CantSpamMensual> getCantidadDeSolicitudesSpam();
}
