package ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.repositories;

import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.apis.OrigenHecho;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.EstadoHecho;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IHechosRepository extends JpaRepository<Hecho, Long> {
    @Query(value = "SELECT h FROM Hecho h WHERE h.fecha_modificacion > ?1 AND h.estado = ?2")
    List<Hecho> findSinceLastUpdate(LocalDateTime last_update, EstadoHecho estado);

    List<Hecho> findAllByEstado(EstadoHecho estado);

    Optional<Hecho> findHechoByOrigenAndIdExterno(OrigenHecho origen, Long idExterno);
}
