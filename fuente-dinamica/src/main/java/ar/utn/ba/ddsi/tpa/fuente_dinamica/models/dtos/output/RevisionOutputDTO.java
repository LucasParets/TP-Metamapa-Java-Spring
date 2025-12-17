package ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.output;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.revision.EstadoRevision;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RevisionOutputDTO {
    private Long idRevision;
    private EstadoRevision estadoRevision;
    private String sugerencia;
    private LocalDateTime fechaRevision;
    private Long idHecho;
    private String tituloHecho;
    private LocalDateTime fechaCreacionHecho;
    private String nombreUsuario;
    private String nombreAdmin;
}
