package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.fuentes.TipoFuente;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.InstantSyncFuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IInstantSyncFuenteRepository extends JpaRepository<InstantSyncFuente, TipoFuente> {
    Optional<InstantSyncFuente> findByTipoFuente(TipoFuente tipoFuente);
}
