package ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Provincia {
    @Id
    private String id;
    @Column(nullable = false)
    private String nombre;
}
