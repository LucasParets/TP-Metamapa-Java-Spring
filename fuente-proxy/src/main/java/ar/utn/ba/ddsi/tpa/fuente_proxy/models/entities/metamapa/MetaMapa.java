package ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.metamapa;

import ar.utn.ba.ddsi.tpa.fuente_proxy.models.dtos.metamapa.*;
import lombok.Data;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Data
public class MetaMapa {
    private String url;
    private WebClient webClient;

    public MetaMapa(String url){
        this.url = url;
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    public List<ColeccionDTO> importarColecciones(){
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/colecciones")
                        .build())
                .retrieve()
                .bodyToFlux(ColeccionDTO.class)
                .collectList()
                .block()
                .stream().map(c -> {
                    c.setInstanciaMetamapa(url);
                    return c;
                }).toList();
    }

    public ColeccionDTO importarColeccion(String handle){
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/colecciones/{handle}/hechos")
                        .build())
                .retrieve()
                .bodyToMono(ColeccionDTO.class)
                .block();
    }

    public HechoMetamapa importarHecho(Long id){
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/hechos/" + id)
                        .build())
                .retrieve()
                .bodyToMono(HechoMetamapa.class)
                .block();
    }

    public PageResponse<HechoMetamapa> importarHechosDeColeccion(String h, FiltrosDTO f, Pageable pg){
        return this.webClient.get()
                .uri(uriBuilder -> {
                    var u = uriBuilder
                            .path("/colecciones/" + h + "/hechos")
                            .queryParamIfPresent("categoria", Optional.ofNullable(f.getCategoria()))
                            .queryParam("page", pg.getPageNumber())
                            .queryParam("size", pg.getPageSize())
                            .queryParamIfPresent("q", Optional.ofNullable(f.getQ()))
                            .queryParamIfPresent("modo_navegacion", Optional.ofNullable(f.getModo_navegacion()))
                            .queryParamIfPresent("fecha_hecho_desde", Optional.ofNullable(f.getFecha_hecho_desde()))
                            .queryParamIfPresent("fecha_hecho_hasta", Optional.ofNullable(f.getFecha_hecho_hasta()));
                    pg.getSort().forEach(s -> {
                        u.queryParam("sort", s.getProperty() + "," + s.getDirection().name().toLowerCase());
                    });
                    return u.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<HechoMetamapa>>() {})
                .block();
    }

    public void enviarSolicitudDeEliminacion(SolicitudDTO solicitud){
        webClient.post()
                .uri("/solicitudes")
                .bodyValue(solicitud)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
