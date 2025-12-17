package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.georef;

import lombok.Data;

import java.util.List;

@Data
public class GeorefBatchResponse {
    List<GeorefResponse> resultados;
}
