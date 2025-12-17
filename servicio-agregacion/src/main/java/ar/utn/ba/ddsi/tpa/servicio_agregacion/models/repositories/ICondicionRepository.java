package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.condiciones.Condicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICondicionRepository extends JpaRepository<Condicion, Long> {
    @Query("SELECT c.criteriosDePertenencia FROM Coleccion c WHERE c.handle = ?1")
    List<Condicion> criteriosDeUnaColeccion(String handle);
}
