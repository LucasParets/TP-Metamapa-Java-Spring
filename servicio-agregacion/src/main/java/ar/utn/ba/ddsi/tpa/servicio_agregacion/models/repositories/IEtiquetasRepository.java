package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IEtiquetasRepository extends JpaRepository<Etiqueta, Long> {
    Optional<Etiqueta> findByNombre(String nombre);

    @Query("SELECT e FROM Hecho h JOIN h.etiquetas e WHERE h.id = ?1")
    List<Etiqueta> findAllByHecho(Long idHecho);
}
