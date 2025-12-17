package ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.hechos;

import ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.fuente.Fuente;
import ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.utilities.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
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
    @Embedded
    private Coordenada coordenada;
    @Column(nullable = false)
    private LocalDateTime fechaHecho;
    @Column(nullable = false)
    private LocalDateTime fechaCarga;
    @Column(nullable = false)
    private LocalDateTime fechaModificacion;
    @Enumerated(EnumType.STRING)
    @Column(name="estado", nullable = false)
    private EstadoHecho estadoDelHecho;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fuente", referencedColumnName = "id", nullable = false)
    private Fuente fuente;
}
