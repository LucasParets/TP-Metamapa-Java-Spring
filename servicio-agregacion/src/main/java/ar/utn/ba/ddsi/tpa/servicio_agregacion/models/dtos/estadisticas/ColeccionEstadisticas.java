package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.estadisticas;

import lombok.Data;

import java.util.Set;

@Data
public class ColeccionEstadisticas {
    private String handle;
    private String titulo;
    private Set<Long> hechos;
}
