package ar.utn.ba.ddsi.tpa.interfaz_web.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudDTO {
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
