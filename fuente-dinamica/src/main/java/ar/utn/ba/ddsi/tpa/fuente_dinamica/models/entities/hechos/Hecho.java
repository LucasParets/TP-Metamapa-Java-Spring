package ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.utilities.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hecho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", referencedColumnName = "id")
    private Categoria categoria;

    @Column(length = 1000)
    private String descripcion;

    @OneToMany
    @JoinColumn(name="id_hecho", referencedColumnName = "id")
    private List<Multimedia> multimedia;

    @Embedded
    private Coordenada coordenada;

    @Column(nullable = false)
    private LocalDateTime fechaHecho;

    @Column(nullable = false)
    private LocalDateTime fechaCarga;

    @Column(nullable = false)
    private LocalDateTime fechaModificacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoHecho estadoDelHecho;

    @Column
    private String nombreUsuario;

    @Column(nullable = false)
    private boolean estaRegistrado;

    private static final int plazoParaModificacion = 7;

    public boolean estaDentroDelPlazoParaModificacion() {
        return ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now()) < plazoParaModificacion;
    }
}
