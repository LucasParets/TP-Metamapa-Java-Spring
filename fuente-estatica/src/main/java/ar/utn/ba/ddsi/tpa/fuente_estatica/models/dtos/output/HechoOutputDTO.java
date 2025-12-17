package ar.utn.ba.ddsi.tpa.fuente_estatica.models.dtos.output;


import ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.utilities.EstadoHecho;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HechoOutputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Float latitud;
    private Float longitud;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaCarga;
    private LocalDateTime fechaModificacion;
    private EstadoHecho estadoDelHecho;
    private String nombreArchivo;
}
