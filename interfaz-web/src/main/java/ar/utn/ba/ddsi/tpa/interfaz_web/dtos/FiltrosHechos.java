package ar.utn.ba.ddsi.tpa.interfaz_web.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FiltrosHechos {
    private String q;
    private String categoria;
    private String modo_navegacion;
    private LocalDateTime fecha_carga_desde;
    private LocalDateTime fecha_carga_hasta;
    private LocalDateTime fecha_hecho_desde;
    private LocalDateTime fecha_hecho_hasta;
}
