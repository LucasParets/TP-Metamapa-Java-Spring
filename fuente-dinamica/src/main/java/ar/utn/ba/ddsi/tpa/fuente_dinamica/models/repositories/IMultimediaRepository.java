package ar.utn.ba.ddsi.tpa.fuente_dinamica.models.repositories;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.Multimedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMultimediaRepository extends JpaRepository<Multimedia, Long> {
}
