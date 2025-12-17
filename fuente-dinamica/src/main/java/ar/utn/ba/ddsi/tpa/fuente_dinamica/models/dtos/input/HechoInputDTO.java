package ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.input;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.Multimedia;
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
    private List<Multimedia> multimedia;
    private LocalDateTime fechaHecho;
    private boolean estaRegistrado;
    private String nombreUsuario;
}
