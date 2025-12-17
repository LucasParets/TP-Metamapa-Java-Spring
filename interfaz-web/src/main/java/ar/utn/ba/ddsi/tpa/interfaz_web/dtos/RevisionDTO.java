package ar.utn.ba.ddsi.tpa.interfaz_web.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RevisionDTO {
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
