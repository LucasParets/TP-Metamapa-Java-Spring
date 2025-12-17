package ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.repositories;

import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.Categoria;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.estadisticas.CategoriaTopMensual;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.estadisticas.HoraPicoMensualCategoria;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.estadisticas.ProvinciaTopCategoriaMensual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICategoriasRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombreIgnoreCase(String nombre);

    @Query(value = """
        SELECT
            sub.anio          AS anio,
            sub.mes           AS mes,
            sub.categoria_nombre AS categoriaNombre,
            sub.total_hechos  AS totalHechos
        FROM (
            SELECT
                YEAR(h.fecha_hecho)      AS anio,
                MONTH(h.fecha_hecho)     AS mes,
                c.id                     AS categoria_id,
                c.nombre                 AS categoria_nombre,
                COUNT(*)                 AS total_hechos,
                ROW_NUMBER() OVER (
                    PARTITION BY YEAR(h.fecha_hecho), MONTH(h.fecha_hecho)
                    ORDER BY COUNT(*) DESC
                ) AS rn
            FROM hecho h
            JOIN categoria c ON c.id = h.id_categoria
            WHERE h.fecha_hecho IS NOT NULL
            GROUP BY YEAR(h.fecha_hecho),
                     MONTH(h.fecha_hecho),
                     c.id,
                     c.nombre
            ) sub
            WHERE sub.rn = 1
            ORDER BY sub.anio, sub.mes;
        """,
            nativeQuery = true)
    List<CategoriaTopMensual> categoriaTopPorMesHistorico();

    @Query(value = """
        SELECT
            sub.anio             AS anio,
            sub.mes              AS mes,
            p.nombre             AS provinciaNombre,
            sub.total_hechos     AS totalHechos
            FROM (
            SELECT
                YEAR(h.fecha_hecho)  AS anio,
                MONTH(h.fecha_hecho) AS mes,
                h.id_provincia       AS id_provincia,
                COUNT(*)             AS total_hechos,
                ROW_NUMBER() OVER (
                    PARTITION BY YEAR(h.fecha_hecho), MONTH(h.fecha_hecho)
                    ORDER BY COUNT(*) DESC
                ) AS rn
            FROM hecho h
            JOIN categoria c ON c.id = h.id_categoria
            WHERE c.id = :categoriaId
            GROUP BY
                YEAR(h.fecha_hecho),
                MONTH(h.fecha_hecho),
                h.id_provincia
            ) sub
            JOIN provincia p ON p.id = sub.id_provincia
            WHERE sub.rn = 1
            ORDER BY sub.anio, sub.mes;
        """,
            nativeQuery = true)
    List<ProvinciaTopCategoriaMensual> provinciaTopPorCategoriaHistorico(@Param("categoriaId") Long categoriaId);

    @Query(value = """
        SELECT
            x.anio          AS anio,
            x.mes           AS mes,
            x.hora          AS hora,
            x.total_hechos  AS totalHechos
        FROM (
            SELECT
                agg.anio,
                agg.mes,
                agg.hora,
                agg.total_hechos,
                ROW_NUMBER() OVER (
                    PARTITION BY agg.anio, agg.mes
                    ORDER BY agg.total_hechos DESC
                ) AS rn
            FROM (
                SELECT
                    YEAR(h.fecha_hecho)  AS anio,
                    MONTH(h.fecha_hecho) AS mes,
                    HOUR(h.fecha_hecho)  AS hora,
                    COUNT(*)             AS total_hechos
                FROM hecho h
                WHERE h.id_categoria = :categoriaId
                GROUP BY
                    YEAR(h.fecha_hecho),
                    MONTH(h.fecha_hecho),
                    HOUR(h.fecha_hecho)
            ) agg
        ) x
        WHERE x.rn = 1
        ORDER BY x.anio, x.mes
        """,
            nativeQuery = true)
    List<HoraPicoMensualCategoria> horaPicoMensualPorCategoria(
            @Param("categoriaId") Long categoriaId
    );
}
