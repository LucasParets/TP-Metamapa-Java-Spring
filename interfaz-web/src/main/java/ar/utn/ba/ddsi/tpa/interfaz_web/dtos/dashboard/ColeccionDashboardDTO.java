package ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dashboard;

import lombok.Data;

@Data
public class ColeccionDashboardDTO {
    private String handle;
    private String titulo;
    private int cant_hechos;
}
