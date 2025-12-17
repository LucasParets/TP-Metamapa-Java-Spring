package ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dinamica;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.EstadoSolicitud;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudModificacionDTO {
    private Long id;
    private EstadoSolicitud estado;
    private LocalDateTime fechaSolicitud;
    private Long idHecho;
    private String tituloNuevo;
    private String descripcionNueva;
    private String categoriaNueva;
    private Float latitudNueva;
    private Float longitudNueva;
    private LocalDateTime fechaHechoNueva;
    private LocalDateTime fechaModificacion;
    private String nombreAdmin;
}
