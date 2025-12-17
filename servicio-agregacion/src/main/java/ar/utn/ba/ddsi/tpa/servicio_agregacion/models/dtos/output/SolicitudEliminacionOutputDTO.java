package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.EstadoSolicitud;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudEliminacionOutputDTO {
    private Long id;
    private Long idHecho;
    private String tituloHecho;
    private String descripcion;
    private EstadoSolicitud estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaResolucion;
    private String nombreUsuario;
    private String nombreAdmin;
}
