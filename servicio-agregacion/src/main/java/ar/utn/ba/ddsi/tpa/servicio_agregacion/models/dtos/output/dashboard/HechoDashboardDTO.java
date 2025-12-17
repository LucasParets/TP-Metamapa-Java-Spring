package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.dashboard;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HechoDashboardDTO {
    private Long id;
    private String titulo;
    private LocalDateTime fecha_carga;
}
