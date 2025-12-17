package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.estadisticas;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudEstadisticas {
    private Long id;
    private Boolean esSpam;
    private LocalDateTime fechaResolucion;

    public SolicitudEstadisticas(Long id, Boolean esSpam, LocalDateTime fechaResolucion) {
        this.id = id;
        this.esSpam = esSpam;
        this.fechaResolucion = fechaResolucion;
    }
}
