package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.consenso;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public interface AlgoritmoConsenso {
    Specification<Hecho> hechosQueCumplen(Long cantFuentes, String handle);
}
