package ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.repositories;

import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.Coleccion;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.estadisticas.ProvinciaTopPorColeccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IColeccionRepository extends JpaRepository<Coleccion, String> {
    Optional<Coleccion> findByHandle(String handle);

    @Query(value = """
        SELECT
            sub.coleccion_id   AS coleccionHandle,
            c.titulo           AS coleccionTitulo,
            p.nombre           AS provinciaNombre,
            sub.total_hechos   AS totalHechos
        FROM (
            SELECT
                hc.coleccion_id,
                h.id_provincia,
                COUNT(*) AS total_hechos,
                ROW_NUMBER() OVER (
                    PARTITION BY hc.coleccion_id
                    ORDER BY COUNT(*) DESC
                ) AS rn
            FROM coleccion_hechos hc
            JOIN hecho h ON h.id = hc.hecho_id
            WHERE h.id_provincia IS NOT NULL
              AND h.fecha_hecho < :hasta
              AND h.fecha_hecho > :desde
            GROUP BY hc.coleccion_id, h.id_provincia
        ) sub
        JOIN coleccion c ON c.handle = sub.coleccion_id
        JOIN provincia p ON p.id = sub.id_provincia
        WHERE sub.rn = 1
        """,
            nativeQuery = true)
    List<ProvinciaTopPorColeccion> provinciaTopPorColeccion(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta
    );
}
