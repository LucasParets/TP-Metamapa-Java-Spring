package ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.metamapa;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FiltrosMetamapa {
    private String categoria;
    private LocalDateTime fecha_reporte_desde;
    private LocalDateTime fecha_reporte_hasta;
    private LocalDateTime fecha_acontecimiento_desde;
    private LocalDateTime fecha_acontecimiento_hasta;
}
