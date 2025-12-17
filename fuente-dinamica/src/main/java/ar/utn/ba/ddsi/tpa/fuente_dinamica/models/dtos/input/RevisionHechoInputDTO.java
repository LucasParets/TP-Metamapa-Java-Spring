package ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.input;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.revision.EstadoRevision;
import lombok.Data;

@Data
public class RevisionHechoInputDTO {
    private EstadoRevision estado;
    private String sugerencia;
    private Long idHecho;
    private String adminUsuario;
}
