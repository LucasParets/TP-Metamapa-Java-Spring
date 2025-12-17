package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Multimedia;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.EstadoHecho;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HechoFuenteDinamicaDTO  {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private List<Multimedia> multimedia;
    private Float latitud;
    private Float longitud;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaCarga;
    private LocalDateTime fechaModificacion;
    private EstadoHecho estadoDelHecho;
    private String nombreUsuario;
    private boolean estaRegistrado;
}
