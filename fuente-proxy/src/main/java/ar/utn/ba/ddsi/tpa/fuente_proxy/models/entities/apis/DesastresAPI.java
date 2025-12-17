package ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.apis;

import ar.utn.ba.ddsi.tpa.fuente_proxy.models.response.Desastre;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.response.DesastresResponse;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.Hecho;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class DesastresAPI extends API{
    private final String token;
    private final WebClient webClient;
    private static final int max_per_page = 100;

    public DesastresAPI(@Value("${desastres.api.url}") String url,
                        @Value("${desastres.api.token}") String token){
        this.token = token;
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    // Resolver el manejo de muchas paginas, con pocas paginas funciona bien
    @Override
    public Flux<Hecho> importarHechos(){
        return importarHechosPagina(1)
                .expand(res -> {
                    if (res.getCurrent_page() < res.getLast_page()) {
                        return importarHechosPagina(res.getCurrent_page() + 1);
                    } else {
                        return Mono.empty();
                    }
                })
                .flatMap(res ->
                        Flux.fromIterable(res.getData().stream()
                                .map(Desastre::convertirEnHecho).toList()));
    }

    private Mono<DesastresResponse> importarHechosPagina(int pagina){
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/desastres")
                        .queryParam("per_page", max_per_page)
                        .queryParam("page", pagina)
                        .build())
                .headers(httpHeaders -> httpHeaders.setBearerAuth(this.token))
                .retrieve()
                .bodyToMono(DesastresResponse.class);
    }

    public OrigenHecho getNombre() {
        return OrigenHecho.DESASTRES_NATURALES;
    }
}
