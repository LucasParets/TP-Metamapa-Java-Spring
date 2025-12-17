package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.consenso;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenDelHecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.utils.HechoSpecs;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MayoriaSimple implements AlgoritmoConsenso {
    @Override
    public Specification<Hecho> hechosQueCumplen(Long cantFuentes, String handle) {
        return Specification.where(HechoSpecs.repetidosEnOtrasFuentes(cantFuentes / 2L + 1L, handle));
    }
}
