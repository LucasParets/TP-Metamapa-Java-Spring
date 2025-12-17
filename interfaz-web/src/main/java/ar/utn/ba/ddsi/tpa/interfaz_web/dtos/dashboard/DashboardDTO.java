package ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dashboard;

import lombok.Data;

import java.util.List;

@Data
public class DashboardDTO {
    private Long cant_hechos;
    private Long cant_colecciones;
    private Long cant_solicitudes;
    private List<ColeccionDashboardDTO> colecciones_top_5_x_cant_hechos;
    private List<HechoDashboardDTO> ultimos_5_hechos_agregados;
}
