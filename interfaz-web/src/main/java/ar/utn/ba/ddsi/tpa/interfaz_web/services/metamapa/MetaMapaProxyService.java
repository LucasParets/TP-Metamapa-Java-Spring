package ar.utn.ba.ddsi.tpa.interfaz_web.services.metamapa;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.FiltrosHechos;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.PageResponse;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.SolicitudDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input.SolicitudInputDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.metamapa.ColeccionMetamapaDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.metamapa.HechoMetamapaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class MetaMapaProxyService {
    private final String urlProxy;
    private final WebClient wc;

    public MetaMapaProxyService(@Value("${api-gateway.url}") String apiGateway) {
        this.urlProxy = apiGateway + "/proxy/metamapa";
        this.wc = WebClient.builder().baseUrl(urlProxy).build();
    }

    public PageResponse<ColeccionMetamapaDTO> getColecciones(Pageable pg) {
        return wc.get().uri(ub -> ub
                .path("/colecciones")
                .queryParam("page", pg.getPageNumber())
                .queryParam("size", pg.getPageSize())
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<ColeccionMetamapaDTO>>() {})
                .block();
    }

    public ColeccionMetamapaDTO getColeccion(String handle, String metamapa) {
        return wc.get().uri(ub -> ub
                        .path("/colecciones/" + handle)
                        .queryParam("metamapa", metamapa)
                        .build())
                .retrieve()
                .bodyToMono(ColeccionMetamapaDTO.class)
                .block();
    }

    public HechoMetamapaDTO getHecho(Long id, String metamapa) {
        return wc.get().uri(ub -> ub
                        .path("/hechos/" + id)
                        .queryParam("metamapa", metamapa)
                        .build())
                .retrieve()
                .bodyToMono(HechoMetamapaDTO.class)
                .block();
    }

    public PageResponse<HechoMetamapaDTO> getHechos(String h, String metamapa, FiltrosHechos f, Pageable pg) {
        return this.wc.get()
                .uri(uriBuilder -> {
                    var u = uriBuilder
                            .path("/colecciones/" + h + "/hechos")
                            .queryParamIfPresent("categoria", Optional.ofNullable(f.getCategoria()))
                            .queryParam("page", pg.getPageNumber())
                            .queryParam("size", pg.getPageSize())
                            .queryParam("metamapa", metamapa)
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
                .bodyToMono(new ParameterizedTypeReference<PageResponse<HechoMetamapaDTO>>() {})
                .block();
    }

    public void crearSolicitud(SolicitudInputDTO s, String metamapa) {
        wc.post()
                .uri(ub -> ub
                        .path("/solicitudes")
                        .queryParam("metamapa", metamapa)
                        .build())
                .bodyValue(s)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
