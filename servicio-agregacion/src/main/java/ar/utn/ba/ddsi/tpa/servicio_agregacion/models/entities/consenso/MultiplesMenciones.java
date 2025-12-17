package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.consenso;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenDelHecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.IColeccionRepository;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.utils.HechoSpecs;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MultiplesMenciones implements AlgoritmoConsenso {

    @Override
    public Specification<Hecho> hechosQueCumplen(Long cantFuentes, String handle) {
        return Specification.where(HechoSpecs.repetidosEnOtrasFuentes(2L, handle))
                .and(HechoSpecs.sinRepetidosQueNoCoinciden(handle));
    }
}
