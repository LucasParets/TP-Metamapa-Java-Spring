package ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.apis;

import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.Hecho;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public abstract class API {
    public abstract Flux<Hecho> importarHechos();
    public abstract OrigenHecho getNombre();
}
