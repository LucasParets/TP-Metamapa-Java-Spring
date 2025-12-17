package ar.utn.ba.ddsi.tpa.fuente_dinamica.models.repositories;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.EstadoSolicitud;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.SolicitudModificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISolicitudModificacionRepsoitory extends JpaRepository<SolicitudModificacion, Long> {
    Page<SolicitudModificacion> findAllByEstado(Pageable pg, EstadoSolicitud estado);
}
