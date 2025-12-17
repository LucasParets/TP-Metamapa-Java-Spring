package ar.utn.ba.ddsi.tpa.fuente_dinamica.models.repositories;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.revision.RevisionHecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRevisionRepository extends JpaRepository<RevisionHecho, Long>, JpaSpecificationExecutor<RevisionHecho> {
    @Query(value = "SELECT r FROM RevisionHecho r WHERE r.estado = 'PENDIENTE'")
    List<RevisionHecho> revisionesPendientes();

    @Query(value = "SELECT r FROM RevisionHecho r WHERE r.hecho.id = ?1")
    RevisionHecho findByHechoId(Long id);


}
