package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.condiciones.*;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.*;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenDelHecho;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Hecho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idCargadoEnOrigen;

    @Column(nullable = false)
    private String titulo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categoria", referencedColumnName = "id", nullable = false)
    private Categoria categoria;

    @Column(length = 1000)
    private String descripcion;

    @OneToMany
    @JoinColumn(name="id_hecho", referencedColumnName = "id")
    private List<Multimedia> multimedia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ciudad", referencedColumnName = "id")
    private Departamento departamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_provincia", referencedColumnName = "id")
    private Provincia provincia;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_origen", referencedColumnName = "id", nullable = false)
    private OrigenDelHecho origen;

    @ManyToMany
    @JoinTable(name = "EtiquetasxHecho",
               joinColumns = @JoinColumn(name = "id_hecho", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "id_etiqueta", referencedColumnName = "id"))
    private List<Etiqueta> etiquetas;

    @Column
    private Boolean geocodificado;

    public void agregarEtiquetas(List<Etiqueta> etiquetasAgregadas){
        etiquetas.addAll(etiquetasAgregadas);
    }

    public void agregarEtiqueta(Etiqueta etiquetaAgregada){
        etiquetas.add(etiquetaAgregada);
    }

    public void eliminarEtiqueta(Etiqueta etiquetaEliminada){
        etiquetas.remove(etiquetaEliminada);
    }

    public Set<Condicion> condicionesParaQueCoincidan() {
        return Set.of(
                new CondicionRangoDistancia(this.getCoordenada(), 0.5),
                new CondicionTituloExacto(this.getTitulo()),
                new CondicionFechaHechoExacta(this.getFechaHecho()),
                new CondicionCategoriaExacta(this.categoria),
                new CondicionEstadoActivo(),
                new CondicionFuenteDiferente(this.origen)
        );
    }
}
