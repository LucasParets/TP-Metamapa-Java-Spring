package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.dashboard;

import lombok.Data;

@Data
public class ColeccionDashboardDTO {
    private String handle;
    private String titulo;
    private int cant_hechos;
}
