package ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.repositories;

import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IHechosRepository extends JpaRepository<Hecho, Long> {
    @Query(value = "SELECT MAX(fechaModificacion) FROM Hecho")
    LocalDateTime findMaxFechaModificacion();
}
