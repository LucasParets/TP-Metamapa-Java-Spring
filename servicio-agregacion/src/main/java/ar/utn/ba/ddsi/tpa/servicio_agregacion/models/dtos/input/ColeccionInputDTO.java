package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input;

import lombok.Data;

import java.util.List;

@Data
public class ColeccionInputDTO {
    private String titulo;
    private String descripcion;
    private String consenso;
    private List<String> fuentes;
}
