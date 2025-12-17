package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output;

import lombok.Data;

import java.util.List;

@Data
public class ColeccionOutputDTO {
    private String handle;
    private String titulo;
    private String descripcion;
    private String consenso;
    private List<String> fuentes;
    private Boolean esDestacada;
}
