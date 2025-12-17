package ar.utn.ba.ddsi.tpa.interfaz_web.services.admin;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.*;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dinamica.HechoDinamicaDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dinamica.RevisionHechoInputDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input.SolicitudInputDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class NotificacionesApiService {
    private final String agregadorServiceUrl;
    private final String dinamicaServiceUrl;
    private final WebApiCallerService api;

    public NotificacionesApiService(@Value("${api-gateway.url}") String apiGatewayUrl,
                              WebApiCallerService api) {
        this.agregadorServiceUrl = apiGatewayUrl + "/agregador/admin/solicitudes";
        this.dinamicaServiceUrl = apiGatewayUrl + "/dinamica";
        this.api = api;
    }

    public PageResponse<SolicitudDTO> getSolicitudesDeEliminacion(Pageable pg, EstadoSolicitud estado) {
        WebClient wc = WebClient.builder().baseUrl(agregadorServiceUrl).build();
        return api.executeWithTokenRetry(accessToken -> wc.get()
                .uri(ub -> {
                    var u = ub
                            .queryParam("page", pg.getPageNumber())
                            .queryParam("size", pg.getPageSize())
                            .queryParam("estado", estado);
                    return u.build();
                })
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<SolicitudDTO>>() {})
                .block());
    }

    public void aprobarSolicitudDeEliminacion(Long idSolicitud, String admin) {
        api.patch(agregadorServiceUrl + "/" + idSolicitud + "/aprobar", admin, void.class);
    }
    public void rechazarSolicitudDeEliminacion(Long idSolicitud, String admin) {
        api.patch(agregadorServiceUrl + "/" + idSolicitud + "/rechazar", admin, void.class);
    }

    public PageResponse<RevisionDTO> getRevisiones(Pageable pg, EstadoRevision estado) {
        WebClient wc = WebClient.builder().baseUrl(dinamicaServiceUrl + "/admin/revisiones").build();
        return api.executeWithTokenRetry(accessToken -> wc.get()
                .uri(ub -> {
                    var u = ub
                            .queryParam("page", pg.getPageNumber())
                            .queryParam("size", pg.getPageSize())
                            .queryParam("estado", estado);
                    return u.build();
                })
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<RevisionDTO>>() {})
                .block());
    }

    public void resolverRevision(Long idRevision, RevisionHechoInputDTO re) {
        api.post(dinamicaServiceUrl + "/admin/revisiones/" + idRevision + "/resolver", re, void.class);
    }

    public HechoDinamicaDTO getHecho(Long idHecho) {
        return api.get(dinamicaServiceUrl + "/hechos/" + idHecho, HechoDinamicaDTO.class);
    }
}
