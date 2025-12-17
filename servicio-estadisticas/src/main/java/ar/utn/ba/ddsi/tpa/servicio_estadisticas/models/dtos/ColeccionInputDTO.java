package ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class ColeccionInputDTO {
    private String handle;
    private String titulo;
    private Set<Long> hechos;
}
