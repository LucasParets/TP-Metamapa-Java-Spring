package ar.utn.ba.ddsi.tpa.interfaz_web.services;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.ColeccionDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.HechoDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.PageResponse;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dinamica.HechoDinamicaDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input.HechoInputDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.usuario.HechoUsuarioDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
public class DinamicaApiService {
    private final String url;
    private final WebApiCallerService api;

    public DinamicaApiService(@Value("${api-gateway.url}") String apiGatewayUrl,
                              WebApiCallerService api) {
        this.api = api;
        this.url = apiGatewayUrl + "/dinamica";
    }

    public void crearHecho(HechoInputDTO dto) {
        try {
            this.api.post(this.url + "/hechos", dto, void.class);
        }
        catch (Exception e) {
            throw new RuntimeException("Error al crear hecho: " + e.getMessage());
        }
    }

    public HechoDinamicaDTO getHecho(Long id) {
        return this.api.get(this.url + "/hechos/" + id, HechoDinamicaDTO.class);
    }

    public void actualizarHecho(Long id, HechoInputDTO dto) {
        this.api.patch(this.url + "/hechos/" + id, dto, void.class);
    }

    public PageResponse<HechoUsuarioDTO> verHechosUsuario(Pageable pg) {
        WebClient wc = WebClient.builder().baseUrl(url + "/usuario/mis-hechos").build();
        return api.executeWithTokenRetry(accessToken -> wc.get()
                .uri(ub -> {
                    var u = ub
                            .queryParam("page", pg.getPageNumber())
                            .queryParam("size", pg.getPageSize());
                    return u.build();
                })
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<HechoUsuarioDTO>>() {})
                .block());
    }
}
