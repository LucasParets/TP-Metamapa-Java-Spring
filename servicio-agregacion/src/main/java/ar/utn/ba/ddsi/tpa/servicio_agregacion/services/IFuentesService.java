package ar.utn.ba.ddsi.tpa.servicio_agregacion.services;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.fuentes.TipoFuente;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public interface IFuentesService {
    boolean traerHechosDeFuenteEstatica();
    boolean traerHechosDeFuenteDinamica();
    boolean traerHechosDeFuenteProxy();
    String getUrl(TipoFuente fuente);
    int getCantFuentes();
    WebClient getFuenteWebClient(TipoFuente fuente);
}
