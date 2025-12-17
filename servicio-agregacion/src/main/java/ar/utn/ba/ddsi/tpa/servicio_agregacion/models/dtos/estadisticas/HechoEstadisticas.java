package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.estadisticas;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Categoria;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.EstadoHecho;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HechoEstadisticas {
    private Long id;
    private String titulo;
    private Long categoria_id;
    private String categoria_nombre;
    private String id_provincia;
    private EstadoHecho estado;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaModificacion;

    public HechoEstadisticas(Long id, String titulo, Long categoria_id, String categoria_nombre, String id_provincia, EstadoHecho estado, LocalDateTime fechaHecho, LocalDateTime fechaModificacion) {
        this.id = id;
        this.titulo = titulo;
        this.categoria_id = categoria_id;
        this.categoria_nombre = categoria_nombre;
        this.id_provincia = id_provincia;
        this.estado = estado;
        this.fechaHecho = fechaHecho;
        this.fechaModificacion = fechaModificacion;
    }
}
