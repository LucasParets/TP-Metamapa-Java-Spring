package ar.utn.ba.ddsi.tpa.interfaz_web.services.admin;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.ColeccionDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.HechoDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.PageResponse;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.RevisionDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input.CriterioDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.exceptions.NotFoundException;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class AdminColeccionApiService {
    private final String agregadorServiceUrl;
    private final WebApiCallerService api;

    public AdminColeccionApiService(@Value("${api-gateway.url}") String apiGatewayUrl,
                                    WebApiCallerService api) {
        this.agregadorServiceUrl = apiGatewayUrl + "/agregador/admin/colecciones";
        this.api = api;
    }

    public PageResponse<ColeccionDTO> getColecciones(String q, Pageable pg) {
        WebClient wc = WebClient.builder().baseUrl(agregadorServiceUrl).build();
        return api.executeWithTokenRetry(accessToken -> wc.get()
                .uri(ub -> {
                    var u = ub
                            .queryParam("page", pg.getPageNumber())
                            .queryParam("size", pg.getPageSize())
                            .queryParam("q", q);
                    return u.build();
                })
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<ColeccionDTO>>() {})
                .block());
    }

    public ColeccionDTO getColeccion(String handle) {
        ColeccionDTO response =
                api.get(agregadorServiceUrl + "/" + handle, ColeccionDTO.class);
        if (response == null) {
            throw new NotFoundException("coleccion", handle);
        }
        return response;
    }

    public void crearColeccion(ColeccionInputDTO dto) {
        api.post(agregadorServiceUrl, dto, void.class);
    }

    public void eliminarColeccion(String handle) {
        api.delete(agregadorServiceUrl + "/" + handle);
    }

    public void actualizarColeccion(String handle, ColeccionInputDTO dto) {
        api.patch(agregadorServiceUrl + "/" + handle, dto, void.class);
    }

    public List<CriterioDTO> getCriteriosDeColeccion(String handle) {
        List<CriterioDTO> response =
                api.getList(agregadorServiceUrl + "/" + handle + "/criterios", CriterioDTO.class);
        if (response == null) {
            throw new NotFoundException("coleccion", handle);
        }
        return response;
    }

    public void agregarCriterioAColeccion(String handle, CriterioDTO dto) {
        api.post(agregadorServiceUrl + "/" + handle + "/criterios", dto, ColeccionDTO.class);
    }

    public void quitarCriterioAColeccion(String handle, Long idCriterio) {
        api.delete(agregadorServiceUrl + "/" + handle + "/criterios/" + idCriterio);
    }

    public void destacarColeccion(String handle) {
        api.post(agregadorServiceUrl + "/" + handle + "/destacada", "", void.class);
    }

    public void quitarDestacadoColeccion(String handle) {
        api.delete(agregadorServiceUrl + "/" + handle + "/destacada");
    }
}
