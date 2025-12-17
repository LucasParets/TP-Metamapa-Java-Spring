package ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.dtos;

import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.Categoria;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.EstadoHecho;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.Provincia;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HechoInputDTO {
    private Long id;
    private String titulo;
    private Long categoria_id;
    private String categoria_nombre;
    private String id_provincia;
    private EstadoHecho estado;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaModificacion;
}
