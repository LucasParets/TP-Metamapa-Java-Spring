package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.georef;

import lombok.Data;

@Data
public class GeorefResponse {
    private GeorefParametros parametros;
    private GeorefUbicacion ubicacion;
}
