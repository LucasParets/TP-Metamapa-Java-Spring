package ar.utn.ba.ddsi.tpa.fuente_proxy.models.dtos.metamapa;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FiltrosDTO {
    private String q;
    private String categoria;
    private String modo_navegacion;
    private LocalDateTime fecha_carga_desde;
    private LocalDateTime fecha_carga_hasta;
    private LocalDateTime fecha_hecho_desde;
    private LocalDateTime fecha_hecho_hasta;
}
