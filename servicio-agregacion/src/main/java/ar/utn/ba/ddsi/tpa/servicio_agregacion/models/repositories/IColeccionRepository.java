package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories;


import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.fuentes.TipoFuente;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Coleccion;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IColeccionRepository extends JpaRepository<Coleccion, String>, JpaSpecificationExecutor<Coleccion> {
    @Query("SELECT c FROM Coleccion c ORDER BY SIZE(c.hechos) DESC LIMIT 5")
    List<Coleccion> top_5_colecciones_x_cant_hechos(PageRequest pr);

    @Query("SELECT count(DISTINCT h.origen) FROM Coleccion c JOIN c.hechos h WHERE c.handle = ?1")
    Long cantFuentes(String handle);

    @Query("UPDATE Coleccion c SET c.esDestacado = :esDestacado WHERE c.handle = :handle")
    @Modifying
    @Transactional
    void cambiarEsDestacado(@Param("handle") String handle, @Param("esDestacado") boolean esDestacado);

    @Query("SELECT c FROM Coleccion c WHERE c.algoritmoConsenso IS NOT NULL")
    List<Coleccion> coleccionConConsenso();

    @Query("SELECT c FROM Coleccion c WHERE ?1 MEMBER OF c.fuentesPermitidas")
    List<Coleccion> coleccionesSegunFuente(TipoFuente tipoFuente);

    @Query("SELECT h.id FROM Coleccion c JOIN c.hechos h WHERE c.handle = ?1")
    Set<Long> findAllHechosIdsByHandle(String handle);
}
