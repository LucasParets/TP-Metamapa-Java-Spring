package ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.repositories;

import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
    @Query(value = "SELECT c FROM Categoria c WHERE trim(lower(c.nombre)) = ?1")
    Optional<Categoria> findByNombre(String nombre);

    @Query(value = "SELECT c.categoria FROM CategoriaAlterna c WHERE trim(lower(c.nombre_alterno)) = ?1")
    Optional<Categoria> findByNombreAlterno(String nombre);
}
