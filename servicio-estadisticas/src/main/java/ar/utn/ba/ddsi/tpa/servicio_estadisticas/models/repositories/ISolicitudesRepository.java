package ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.repositories;

import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.SolicitudEliminacion;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.estadisticas.CantSpamMensual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ISolicitudesRepository extends JpaRepository<SolicitudEliminacion, Long> {
    @Query(value = """
        SELECT
            YEAR(s.fecha_resolucion)  AS anio,
            MONTH(s.fecha_resolucion) AS mes,
            SUM(CASE WHEN s.es_spam = TRUE THEN 1 ELSE 0 END) AS totalSpam
        FROM solicitud_eliminacion s
        GROUP BY YEAR(s.fecha_resolucion), MONTH(s.fecha_resolucion)
        ORDER BY anio, mes
        """,
            nativeQuery = true)
    List<CantSpamMensual> spamPorMes();

    @Query(value = "SELECT MAX(fechaResolucion) FROM SolicitudEliminacion")
    LocalDateTime findMaxFechaResolucion();
}
