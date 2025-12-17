package ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudInputDTO {
    private Long id;
    private Boolean esSpam;
    private LocalDateTime fechaResolucion;
}
