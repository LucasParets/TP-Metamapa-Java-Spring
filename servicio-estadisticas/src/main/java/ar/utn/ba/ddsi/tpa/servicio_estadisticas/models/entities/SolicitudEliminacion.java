package ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class SolicitudEliminacion {
    @Id
    private Long id;

    @Column
    private Boolean esSpam;

    @Column
    private LocalDateTime fechaResolucion;
}
