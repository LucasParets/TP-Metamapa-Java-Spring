package ar.utn.ba.ddsi.tpa.fuente_proxy.models.dtos.metamapa;

import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.EstadoHecho;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HechoMetamapa {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private List<MultimediaDTO> multimedia;
    private List<String> etiquetas;
    private Float latitud;
    private Float longitud;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaCarga;
    private LocalDateTime fechaModificacion;
    private EstadoHecho estadoDelHecho;
    private String origenHecho;
    private String fuente;
}
