package ar.utn.ba.ddsi.tpa.fuente_estatica.models.repositories;

import ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.fuente.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IFuentesRepository extends JpaRepository<Fuente, Long> {

}
