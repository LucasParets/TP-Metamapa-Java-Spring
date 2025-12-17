package ar.utn.ba.ddsi.tpa.interfaz_web.dtos.metamapa;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dinamica.MultimediaDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HechoMetamapaDTO {
    private Long id;
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
    private String estadoDelHecho;
    private String origenHecho;
    private String fuente;
}
