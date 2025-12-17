package ar.utn.ba.ddsi.tpa.fuente_dinamica.services.impl;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.input.HechoInputDTO;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.output.SolicitudModificacionDTO;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.Categoria;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.EstadoSolicitud;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.SolicitudModificacion;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.repositories.ISolicitudModificacionRepsoitory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SolicitudesService {
    private final ISolicitudModificacionRepsoitory solicitudRepsoitory;
    private final IHechosRepository hechosRepository;

    public Page<SolicitudModificacionDTO> getSolicitudes(Pageable pg, EstadoSolicitud estado) {
        if (estado != null)
            return solicitudRepsoitory.findAllByEstado(pg, estado).map(this::solicitudADTO);
        return solicitudRepsoitory.findAll(pg).map(this::solicitudADTO);
    }

    @Transactional
    public void aceptarSolicitud(Long id, String admin){
        SolicitudModificacion solicitud = solicitudRepsoitory.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontro la solicitud con el id " + id));

        Hecho hecho = hechosRepository.findById(solicitud.getHecho().getId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontro el hecho asociado a la solicitud"));

        actualizarHecho(hecho, solicitud);

        solicitud.setFechaModificacion(LocalDateTime.now());
        solicitud.setEstado(EstadoSolicitud.ACEPTADA);
        solicitud.setNombreAdmin(admin);
    }

    @Transactional
    public void rechazarSolicitud(Long id, String admin){
        SolicitudModificacion solicitud = solicitudRepsoitory.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontro la solicitud con el id " + id));

        solicitud.setFechaModificacion(LocalDateTime.now());
        solicitud.setEstado(EstadoSolicitud.RECHAZADA);
        solicitud.setNombreAdmin(admin);
    }

    private void actualizarHecho(Hecho hecho, SolicitudModificacion s) {
        if (s.getTituloNuevo() != null) {
            hecho.setTitulo(s.getTituloNuevo());
        }
        if (s.getDescripcionNueva() != null) {
            hecho.setDescripcion(s.getDescripcionNueva());
        }
        if (s.getCategoriaNueva() != null) {
            hecho.setCategoria(s.getCategoriaNueva());
        }
        if (s.getLatitudNueva() != null) {
            hecho.getCoordenada().setLatitud(s.getLatitudNueva());
        }
        if (s.getLongitudNueva() != null) {
            hecho.getCoordenada().setLongitud(s.getLongitudNueva());
        }
        if (s.getFechaHechoNueva() != null) {
            hecho.setFechaHecho(s.getFechaHechoNueva());
        }
        hecho.setFechaModificacion(LocalDateTime.now());
        hechosRepository.save(hecho);
    }

    private SolicitudModificacionDTO solicitudADTO(SolicitudModificacion s) {
        SolicitudModificacionDTO dto = new SolicitudModificacionDTO();
        dto.setId(s.getId());
        dto.setEstado(s.getEstado());
        dto.setFechaSolicitud(s.getFechaSolicitud());
        dto.setIdHecho(s.getHecho().getId());
        dto.setTituloNuevo(s.getTituloNuevo());
        dto.setDescripcionNueva(s.getDescripcionNueva());
        dto.setCategoriaNueva(s.getCategoriaNueva() == null ? null : s.getCategoriaNueva().getNombre());
        dto.setLatitudNueva(s.getLatitudNueva());
        dto.setLongitudNueva(s.getLongitudNueva());
        dto.setFechaHechoNueva(s.getFechaHechoNueva());
        dto.setFechaModificacion(s.getFechaModificacion());
        dto.setNombreAdmin(s.getNombreAdmin());
        return dto;
    }
}
