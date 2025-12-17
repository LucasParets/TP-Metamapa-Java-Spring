package ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho;

import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.apis.OrigenHecho;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Hecho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String titulo;
    @Column(nullable = false, length = 1000)
    private String descripcion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hecho_id", referencedColumnName = "id")
    private Categoria categoria;
    @Embedded
    private Coordenada coordenada;
    @Column(nullable = false)
    private LocalDateTime fecha_hecho;
    @Column(nullable = false)
    private LocalDateTime fecha_creacion;
    @Column(nullable = false)
    private LocalDateTime fecha_modificacion;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoHecho estado;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrigenHecho origen;
    @Column
    private Long idExterno;
}
