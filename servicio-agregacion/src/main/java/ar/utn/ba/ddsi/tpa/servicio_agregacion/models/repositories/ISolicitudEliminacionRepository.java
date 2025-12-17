package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.estadisticas.SolicitudEstadisticas;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.SolicitudEliminacion;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.EstadoSolicitud;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ISolicitudEliminacionRepository extends JpaRepository<SolicitudEliminacion, Long> {
    Page<SolicitudEliminacion> findAllByEstado(Pageable pg, EstadoSolicitud estado);

    List<SolicitudEliminacion> findAllByEstadoAndEsSpamIsNull(EstadoSolicitud estado);

    @Query(value = """
        SELECT new ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.estadisticas.SolicitudEstadisticas(
                s.id, s.esSpam, s.fechaResolucion) FROM SolicitudEliminacion s
                WHERE s.fechaResolucion IS NOT NULL AND s.fechaResolucion > ?1
    """)
    List<SolicitudEstadisticas> findSinceLastFechaResolucion(LocalDateTime last_update);

    @Query("UPDATE SolicitudEliminacion s SET s.estado = :estado, s.fechaResolucion = :fechaResolucion, s.nombreAdministrador = :admin WHERE s.hechoAEliminar.id = :hechoId")
    @Modifying
    @Transactional
    void updateEstadoByHechoId(@Param("hechoId") Long hechoId, @Param("estado") EstadoSolicitud estado, @Param("fechaResolucion") LocalDateTime fechaResolucion, @Param("admin") String admin);
}
