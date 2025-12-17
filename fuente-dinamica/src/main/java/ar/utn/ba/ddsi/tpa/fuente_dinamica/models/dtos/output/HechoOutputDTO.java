package ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.output;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.input.MultimediaInputDTO;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.Multimedia;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.utilities.Coordenada;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.utilities.EstadoHecho;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class HechoOutputDTO {
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
