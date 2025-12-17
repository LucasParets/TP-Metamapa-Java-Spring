package ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Coleccion {
    @Id
    private String handle;

    @Column
    private String titulo;

    @ManyToMany
    @JoinTable(
            name = "coleccion_hechos",
            joinColumns = @JoinColumn(name = "coleccion_id"),
            inverseJoinColumns = @JoinColumn(name = "hecho_id")
    )
    private List<Hecho> hechos;

    public Coleccion() {
        this.hechos = new ArrayList<>();
    }
}
