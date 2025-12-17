package ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dinamica.MultimediaDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HechoInputDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Float latitud;
    private Float longitud;
    private List<MultimediaDTO> multimedia;
    private LocalDateTime fechaHecho;
    private boolean estaRegistrado;
    private String nombreUsuario;
}
