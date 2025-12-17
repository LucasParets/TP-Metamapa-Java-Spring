package ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dinamica;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.EstadoRevision;
import lombok.Data;

@Data
public class RevisionHechoInputDTO {
    private EstadoRevision estado;
    private String sugerencia;
    private Long idHecho;
    private String adminUsuario;
}
