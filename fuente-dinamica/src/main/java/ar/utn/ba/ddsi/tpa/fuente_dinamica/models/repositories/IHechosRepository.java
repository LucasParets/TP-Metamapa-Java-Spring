package ar.utn.ba.ddsi.tpa.fuente_dinamica.models.repositories;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.utilities.EstadoHecho;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IHechosRepository extends JpaRepository<Hecho, Long> {
    @Query(value = "SELECT h FROM Hecho h WHERE h.fechaModificacion > ?1 AND h.estadoDelHecho = ?2")
    List<Hecho> findSinceLastUpdate(LocalDateTime last_update, EstadoHecho estado);

    @Query("UPDATE Hecho h SET h.estadoDelHecho = :estado WHERE h.id = :id")
    @Modifying
    @Transactional
    void desactivarHecho(@Param("id") Long id, @Param("estado") EstadoHecho estado);

    List<Hecho> findAllByEstadoDelHecho(EstadoHecho estado);

    Page<Hecho> findAllByNombreUsuario(String nombreUsuario, Pageable pg);
}
