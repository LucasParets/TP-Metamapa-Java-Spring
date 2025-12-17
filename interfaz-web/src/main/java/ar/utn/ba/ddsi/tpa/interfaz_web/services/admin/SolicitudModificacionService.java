package ar.utn.ba.ddsi.tpa.interfaz_web.services.admin;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.EstadoSolicitud;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.PageResponse;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.SolicitudDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dinamica.SolicitudModificacionDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SolicitudModificacionService {
    private final String dinamicaServiceUrl;
    private final WebApiCallerService api;

    public SolicitudModificacionService(@Value("${api-gateway.url}") String apiGatewayUrl,
                                    WebApiCallerService api) {
        this.dinamicaServiceUrl = apiGatewayUrl + "/dinamica/admin/solicitud-modificacion";
        this.api = api;
    }

    public PageResponse<SolicitudModificacionDTO> getSolicitudes(Pageable pg, EstadoSolicitud estado) {
        WebClient wc = WebClient.builder().baseUrl(dinamicaServiceUrl).build();
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
                .bodyToMono(new ParameterizedTypeReference<PageResponse<SolicitudModificacionDTO>>() {})
                .block());
    }
    public void aprobarSolicitud(Long idSolicitud, String admin) {
        api.post(dinamicaServiceUrl + "/" + idSolicitud + "/aceptar", admin, void.class);
    }
    public void rechazarSolicitud(Long idSolicitud, String admin) {
        api.post(dinamicaServiceUrl + "/" + idSolicitud + "/rechazar", admin, void.class);
    }
}
