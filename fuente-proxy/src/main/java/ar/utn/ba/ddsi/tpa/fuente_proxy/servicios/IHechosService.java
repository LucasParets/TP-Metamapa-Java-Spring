package ar.utn.ba.ddsi.tpa.fuente_proxy.servicios;

import ar.utn.ba.ddsi.tpa.fuente_proxy.models.dtos.HechoDTO;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.Hecho;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IHechosService {
    List<HechoDTO> getHechos(String last_update);
    void importarHechos();
    void desactivarHecho(Long id);
}
