package ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Hecho {
    @Id
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "id_categoria", referencedColumnName = "id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_provincia", referencedColumnName = "id")
    private Provincia provincia;

    @Column(nullable = false)
    private LocalDateTime fechaHecho;

    @Column(nullable = false)
    private LocalDateTime fechaModificacion;
}
