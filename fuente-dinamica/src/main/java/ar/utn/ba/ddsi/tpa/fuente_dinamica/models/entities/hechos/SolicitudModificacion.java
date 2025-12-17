package ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.utilities.Coordenada;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class SolicitudModificacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private EstadoSolicitud estado;

    @Column(nullable = false)
    private LocalDateTime fechaSolicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hecho", referencedColumnName = "id")
    private Hecho hecho;

    @Column
    private String tituloNuevo;

    @Column(length = 1000)
    private String descripcionNueva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria_nueva", referencedColumnName = "id")
    private Categoria categoriaNueva;

    @Column
    private Float latitudNueva;

    @Column
    private Float longitudNueva;

    @Column
    private LocalDateTime fechaHechoNueva;

    @Column
    private LocalDateTime fechaModificacion;

    @Column
    private String nombreAdmin;

    public SolicitudModificacion() {
        this.estado = EstadoSolicitud.PENDIENTE;
        this.fechaSolicitud = LocalDateTime.now();
    }
}
