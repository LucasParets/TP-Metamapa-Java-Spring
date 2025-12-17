package ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.repositories;

import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.Coleccion;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProvinciaRepository extends JpaRepository<Provincia, String> {
    @Query(value = "SELECT p FROM Hecho h JOIN h.provincia p WHERE h.categoria.id = ?1 GROUP BY p ORDER BY COUNT(*) DESC LIMIT 1")
    Provincia findProvinciaConMasHechosEnUnaCategoria(Long id_categoria);
}
