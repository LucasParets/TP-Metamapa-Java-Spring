package ar.utn.ba.ddsi.tpa.fuente_estatica.models.repositories;

import ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IHechosRepository extends JpaRepository<Hecho, Long> {
    @Query(value = "SELECT h FROM Hecho h WHERE h.fechaModificacion > ?1")
    List<Hecho> findSinceLastUpdate(LocalDateTime last_update);
}
