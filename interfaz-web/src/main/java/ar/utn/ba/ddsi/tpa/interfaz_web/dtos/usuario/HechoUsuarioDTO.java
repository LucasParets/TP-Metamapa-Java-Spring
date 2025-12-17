package ar.utn.ba.ddsi.tpa.interfaz_web.dtos.usuario;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.EstadoRevision;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HechoUsuarioDTO {
    private Long idHecho;
    private String titulo;
    private String descripcion;
    private String categoria;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaCarga;
    private String estadoDelHecho;
    private EstadoRevision estadoRevision;
    private LocalDateTime fechaRevision;
    private String sugerencia;
    private String nombreAdmin;
}
