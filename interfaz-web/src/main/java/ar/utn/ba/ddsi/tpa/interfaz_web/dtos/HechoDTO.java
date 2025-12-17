package ar.utn.ba.ddsi.tpa.interfaz_web.dtos;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dinamica.MultimediaDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HechoDTO {
    private Long id;
    private Long idCargadoEnOrigen;
    private String titulo;
    private String descripcion;
    private String categoria;
    private List<MultimediaDTO> multimedia;
    private List<String> etiquetas;
    private Float latitud;
    private Float longitud;
    private String provincia;
    private String departamento;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaCarga;
    private LocalDateTime fechaModificacion;
    private String origenHecho;
    private String fuente;
}
