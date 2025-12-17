package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenDelHecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenDinamica;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenEstatica;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenProxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IOrigenRepository extends JpaRepository<OrigenDelHecho, Long> {
    @Query("SELECT o FROM OrigenEstatica o WHERE o.nombreArchivo = ?1")
    Optional<OrigenEstatica> findByNombreArchivo(String nombreArchivo);

    @Query("SELECT o FROM OrigenDinamica o WHERE o.nombreUsuario = ?1")
    Optional<OrigenDinamica> findByNombreUsuario(String nombreUsuario);

    @Query("SELECT o FROM OrigenProxy o WHERE o.nombreApi = ?1")
    Optional<OrigenProxy> findByNombreApi(String nombreApi);


}
