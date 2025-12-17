package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Multimedia;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.EstadoHecho;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HechoOutputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private List<Multimedia> multimedia;
    private List<String> etiquetas;
    private Float latitud;
    private Float longitud;
    private String provincia;
    private String departamento;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaCarga;
    private LocalDateTime fechaModificacion;
    private EstadoHecho estadoDelHecho;
    private String origenHecho;
    private String fuente;
}
