package ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.output;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.revision.EstadoRevision;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.utilities.EstadoHecho;
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
    private EstadoHecho estadoDelHecho;
    private EstadoRevision estadoRevision;
    private LocalDateTime fechaRevision;
    private String sugerencia;
    private String nombreAdmin;
}
