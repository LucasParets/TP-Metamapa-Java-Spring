package ar.utn.ba.ddsi.tpa.servicio_agregacion.services;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.SolicitudEliminacionOutputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.SolicitudEliminacion;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.EstadoSolicitud;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ISolicitudEliminacionService {
    void cargarSolicitudDeEliminacion(SolicitudEliminacionInputDTO dto);
    void aceptarSolicitudDeEliminacion(Long idSolicitud, String idAdministrador);
    void rechazarSolicitudDeEliminacion(Long idSolicitud, String idAdministrador);
    Page<SolicitudEliminacionOutputDTO> mostrarSolicitudesDeEliminacion(Pageable pg, EstadoSolicitud estado);
    void detectarSpam();
}
