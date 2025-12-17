package ar.utn.ba.ddsi.tpa.interfaz_web.services.admin;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.FiltrosHechos;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.HechoDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.PageResponse;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class HechosService {
    private final String agregadorServiceUrl;
    private final WebApiCallerService api;

    public HechosService(@Value("${api-gateway.url}") String apiGatewayUrl,
                         WebApiCallerService api) {
        this.agregadorServiceUrl = apiGatewayUrl + "/agregador";
        this.api = api;
    }

    public PageResponse<HechoDTO> getHechos(FiltrosHechos f, Pageable pg) {
        WebClient wc = WebClient.builder().baseUrl(agregadorServiceUrl).build();
        return wc.get()
                .uri(ub -> {
                    var u = ub
                            .path("/public/hechos")
                            .queryParam("page", pg.getPageNumber())
                            .queryParam("size", pg.getPageSize())
                            .queryParam("q", f.getQ())
                            .queryParam("categoria", f.getCategoria())
                            .queryParam("modo_navegacion", f.getModo_navegacion())
                            .queryParam("fecha_hecho_desde", f.getFecha_hecho_desde())
                            .queryParam("fecha_hecho_hasta", f.getFecha_hecho_hasta());
                    pg.getSort().forEach(s -> {
                        u.queryParam("sort", s.getProperty() + "," + s.getDirection().name().toLowerCase());
                    });
                    return u.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<HechoDTO>>() {})
                .block();
    }

    public void eliminarHecho(Long id) {
        api.delete(agregadorServiceUrl + "/admin/hechos/" + id);
    }
}
