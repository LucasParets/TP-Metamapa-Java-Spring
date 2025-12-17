package ar.utn.ba.ddsi.tpa.servicio_agregacion.services.impl;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.estadisticas.ColeccionEstadisticas;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.estadisticas.HechoEstadisticas;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.estadisticas.SolicitudEstadisticas;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Coleccion;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.SolicitudEliminacion;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.FechaParser;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.IColeccionRepository;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.ISolicitudEliminacionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class EstadisticasService {
    private final IHechosRepository hechosRepository;
    private final IColeccionRepository coleccionRepository;
    private final ISolicitudEliminacionRepository solicitudRepository;

    public List<HechoEstadisticas> mostrarHechos(String last_update) {
        return hechosRepository.findSinceLastUpdate(FechaParser.of(last_update));
    }

    public List<ColeccionEstadisticas> mostrarColecciones() {
        return coleccionRepository.findAll().stream().map(this::crearColeccionDTO).toList();
    }

    public List<SolicitudEstadisticas> mostrarSolicitudes(String last_update) {
        return solicitudRepository.findSinceLastFechaResolucion(FechaParser.of(last_update));
    }

    private ColeccionEstadisticas crearColeccionDTO(Coleccion c) {
        Set<Long> hechos = coleccionRepository.findAllHechosIdsByHandle(c.getHandle());
        ColeccionEstadisticas dto = new ColeccionEstadisticas();
        dto.setHandle(c.getHandle());
        dto.setTitulo(c.getTitulo());
        dto.setHechos(hechos);
        return dto;
    }
}
