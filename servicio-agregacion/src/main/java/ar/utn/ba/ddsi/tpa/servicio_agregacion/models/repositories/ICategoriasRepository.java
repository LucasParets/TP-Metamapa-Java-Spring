package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ICategoriasRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);
    @Query("""
        select distinct h.categoria
        from Coleccion c
        join c.hechos h
        where c.handle = ?1
        order by h.categoria.nombre asc
    """)
    List<Categoria> findDistinctByColeccionHandle(String handle);
}
