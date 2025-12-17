package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Multimedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IMultimediaRepository extends JpaRepository<Multimedia, Long> {
    Optional<Multimedia> findByPublicId(String publicId);
}
