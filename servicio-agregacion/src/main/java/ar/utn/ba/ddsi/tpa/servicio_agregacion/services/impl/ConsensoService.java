package ar.utn.ba.ddsi.tpa.servicio_agregacion.services.impl;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.consenso.MultiplesMenciones;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Coleccion;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.IColeccionRepository;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.utils.HechoSpecs;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ConsensoService {
    private final IHechosRepository hechosRepository;
    private final IColeccionRepository coleccionRepository;

    @Transactional
    public void aplicarConsenso(Coleccion c) {
        if (c.getAlgoritmoConsenso() != null) {
            Long cantFuentes;

            if (c.getAlgoritmoConsenso() instanceof MultiplesMenciones)
                cantFuentes = 0L;
            else
                cantFuentes = coleccionRepository.cantFuentes(c.getHandle());

            String handle = c.getHandle();

            Specification<Hecho> spec = Specification.where(HechoSpecs.enColeccion(handle))
                    .and(c.getAlgoritmoConsenso().hechosQueCumplen(cantFuentes, handle));

            c.refrescarHechosConsensuados(hechosRepository.findAll(spec));

            coleccionRepository.save(c);
        }
    }
}
