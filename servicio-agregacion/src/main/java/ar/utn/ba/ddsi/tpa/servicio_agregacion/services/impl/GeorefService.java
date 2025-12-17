package ar.utn.ba.ddsi.tpa.servicio_agregacion.services.impl;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.georef.GeorefBatchResponse;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.georef.GeorefRequest;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.georef.GeorefRequestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class GeorefService {
    private WebClient webClient;

    public GeorefService(@Value("${georef.api.url}") String url) {
        this.webClient = WebClient.create(url);
    }

    public GeorefBatchResponse geocodificacionInversa(List<GeorefRequestInfo> ubicaciones) {
        GeorefRequest request = new GeorefRequest();
        request.setUbicaciones(ubicaciones);

        return webClient.post()
                .uri("/ubicacion")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GeorefBatchResponse.class)
                .block();
    }
}
