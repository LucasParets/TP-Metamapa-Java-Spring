package ar.utn.ba.ddsi.tpa.servicio_agregacion.utils;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.FiltroColecciones;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.FiltroHechosMetamapa;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Coleccion;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ColeccionesSpecs {
    public static Specification<Coleccion> filtros(FiltroColecciones f) {
        return (root, cq, cb) -> {
            List<Predicate> p = new ArrayList<>();

            if (f.getQ() != null && !f.getQ().isBlank()) {
                String like = "%"+f.getQ().trim().toLowerCase()+"%";
                p.add(cb.like(cb.lower(root.get("titulo")), like));
            }

            return cb.and(p.toArray(new Predicate[0]));
        };
    }
}
