package ar.utn.ba.ddsi.tpa.servicio_agregacion.services.impl;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.exceptions.hecho.HechoNoEncontradoException;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.exceptions.solicitud.SolicitudNoEncontradaException;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.exceptions.solicitud.SolicitudSpamException;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.SolicitudEliminacionOutputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.SolicitudEliminacion;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.spam.DetectorDeSpam;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.spam.DetectorDeSpamBasico;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.EstadoHecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.EstadoSolicitud;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.ISolicitudEliminacionRepository;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.IFuentesService;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.IHechosService;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.ISolicitudEliminacionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class SolicitudEliminacionService implements ISolicitudEliminacionService {
    private final ISolicitudEliminacionRepository solicitudEliminacionRepository;
    private final IHechosRepository hechosRepository;
    private final DetectorDeSpam detectorDeSpam;
    private final WebClient webClient;
    private final IFuentesService fuentesService;

    public SolicitudEliminacionService(ISolicitudEliminacionRepository solicitudEliminacionRepository,
                                       IHechosRepository hechosRepository,
                                       IFuentesService fuentesService) {
        this.solicitudEliminacionRepository = solicitudEliminacionRepository;
        this.hechosRepository = hechosRepository;
        this.detectorDeSpam = new DetectorDeSpamBasico();
        this.webClient = WebClient.create();
        this.fuentesService = fuentesService;
    }

    @Override
    public void cargarSolicitudDeEliminacion(SolicitudEliminacionInputDTO dto) {
        SolicitudEliminacion solicitud = this.crearSolicitudDeEliminacionDeUnDTO(dto);
        solicitudEliminacionRepository.save(solicitud);
    }

    @Override
    @Transactional
    public void aceptarSolicitudDeEliminacion(Long idSolicitud, String usuarioAdmin) {
        SolicitudEliminacion solicitud = solicitudEliminacionRepository.findById(idSolicitud)
                .orElseThrow(() -> new SolicitudNoEncontradaException("No se encontro la solicitud con el id " + idSolicitud));
        Hecho hecho = hechosRepository.findById(solicitud.getHechoAEliminar().getId())
                .orElseThrow(() -> new HechoNoEncontradoException("No se encontro el hecho con el id " + solicitud.getHechoAEliminar().getId()));

        solicitud.aceptar();

        log.info("Solicitud de eliminación aceptada para el hecho con id: {}", hecho.getId());

        hecho.setFechaModificacion(LocalDateTime.now());
        hecho.setEstadoDelHecho(EstadoHecho.INACTIVO);

        solicitud.setNombreAdministrador(usuarioAdmin);

        solicitudEliminacionRepository.updateEstadoByHechoId(hecho.getId(), EstadoSolicitud.ACEPTADA, solicitud.getFechaResolucion(), usuarioAdmin);

        WebClient webClient = fuentesService.getFuenteWebClient(hecho.getOrigen().getFuente());
        Long idHecho = hecho.getIdCargadoEnOrigen();
        webClient.delete()
                .uri(ub -> ub.path("/hechos/" + idHecho).build())
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void rechazarSolicitudDeEliminacion(Long idSolicitud, String idAdministrador) {
        SolicitudEliminacion solicitud = solicitudEliminacionRepository.findById(idSolicitud)
                .orElseThrow(() -> new SolicitudNoEncontradaException("No se encontro la solicitud con el id " + idSolicitud));

        solicitud.rechazar();

        log.info("Solicitud de eliminación rechazada por {} con solicitud de id: {}", idAdministrador, idSolicitud);

        solicitud.setNombreAdministrador(idAdministrador);

        solicitudEliminacionRepository.save(solicitud);
    }

    private SolicitudEliminacion crearSolicitudDeEliminacionDeUnDTO(SolicitudEliminacionInputDTO dto) {
        Hecho hecho = hechosRepository.findById(dto.getIdHechoAEliminar())
                .orElseThrow(() -> new HechoNoEncontradoException("Hecho no encontrado con el id " + dto.getIdHechoAEliminar()));
        SolicitudEliminacion solicitud = new SolicitudEliminacion(hecho, dto.getDescripcion());
        solicitud.setNombreUsuario(dto.getNombreUsuario());
        log.info("Solicitud de eliminación creada para el hecho con id: {}", hecho.getId());
        return solicitud;
    }

    public Page<SolicitudEliminacionOutputDTO> mostrarSolicitudesDeEliminacion(Pageable pg, EstadoSolicitud estado) {
        log.info("Mostrando solicitudes de eliminación con estado: {}", estado != null ? estado.toString() : "TODOS");
        if (estado != null)
            return solicitudEliminacionRepository.findAllByEstado(pg, estado).map(this::solicitudADTO);
        else return solicitudEliminacionRepository.findAll(pg).map(this::solicitudADTO);
    }

    private SolicitudEliminacionOutputDTO solicitudADTO(SolicitudEliminacion s) {
        SolicitudEliminacionOutputDTO dto = new SolicitudEliminacionOutputDTO();
        dto.setId(s.getId());
        dto.setIdHecho(s.getHechoAEliminar().getId());
        dto.setTituloHecho(s.getHechoAEliminar().getTitulo());
        dto.setFechaCreacion(s.getFechaCreacion());
        dto.setDescripcion(s.getDescripcion());
        dto.setEstado(s.getEstado());
        dto.setFechaResolucion(s.getFechaResolucion());
        dto.setNombreUsuario(s.getNombreUsuario());
        dto.setNombreAdmin(s.getNombreAdministrador());
        return dto;
    }

    @Transactional
    public void detectarSpam() {
        log.info("Detectando spam...");
        List<SolicitudEliminacion> solicitudesPendientes = solicitudEliminacionRepository.findAllByEstadoAndEsSpamIsNull(EstadoSolicitud.PENDIENTE);
        for (SolicitudEliminacion s : solicitudesPendientes) {
            if (detectorDeSpam.esSpam(s.getDescripcion())) {
                s.rechazar();
                s.setNombreAdministrador("detector de spam");
                s.setEsSpam(true);
                log.warn("Se ha detectado spam en la solicitud con id: {}. Se ha rechazado la solicitud y se ha registrado como spam.", s.getId());
            }
            else s.setEsSpam(false);
        }
        log.info("Finalizando detección de spam.");
    }
}
