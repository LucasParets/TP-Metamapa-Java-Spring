package ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dinamica;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HechoDinamicaDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private List<MultimediaDTO> multimedia;
    private Float latitud;
    private Float longitud;
    private String provincia;
    private String departamento;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaCarga;
    private LocalDateTime fechaModificacion;
    private String estadoDelHecho;
    private String nombreUsuario;
    private boolean estaRegistrado;
    private String fuente;
    private String origenHecho;
}
