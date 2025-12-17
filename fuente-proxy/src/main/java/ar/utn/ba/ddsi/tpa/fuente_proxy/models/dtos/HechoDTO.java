package ar.utn.ba.ddsi.tpa.fuente_proxy.models.dtos;

import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.EstadoHecho;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HechoDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Float latitud;
    private Float longitud;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaCarga;
    private LocalDateTime fechaModificacion;
    private EstadoHecho estadoDelHecho;
    private String origenHecho;
}
