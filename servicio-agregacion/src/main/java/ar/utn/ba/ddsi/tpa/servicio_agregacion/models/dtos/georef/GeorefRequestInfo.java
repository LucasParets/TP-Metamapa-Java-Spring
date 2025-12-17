package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.georef;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeorefRequestInfo {
    private Double lat;
    private Double lon;
    private Boolean aplanar = true;
    private String campos = "estandar";

    public GeorefRequestInfo(Double lat, Double lon) {
           this.lat = lat;
           this.lon = lon;
    }
}
