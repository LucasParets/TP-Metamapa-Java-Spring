package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.estadisticas.HechoEstadisticas;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenDelHecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IHechosRepository extends JpaRepository<Hecho, Long>, JpaSpecificationExecutor<Hecho> {
    Optional<Hecho> findByIdCargadoEnOrigenAndOrigen(Long idCargadoEnOrigen, OrigenDelHecho origen);

    List<Hecho> findTop500ByGeocodificadoIsFalse();

    @Query(value = """
        SELECT new ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.estadisticas.HechoEstadisticas(
                    h.id, h.titulo, h.categoria.id, h.categoria.nombre, h.provincia.id,
                    h.estadoDelHecho, h.fechaHecho, h.fechaModificacion)
        FROM Hecho h
        WHERE h.fechaModificacion > ?1""")
    List<HechoEstadisticas> findSinceLastUpdate(LocalDateTime last_update);
}
