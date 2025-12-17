package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.georef;

import lombok.Data;

@Data
public class GeorefUbicacion {
    private Double lat;
    private Double lon;
    private String provincia_id;
    private String provincia_nombre;
    private String departamento_id;
    private String departamento_nombre;
}
