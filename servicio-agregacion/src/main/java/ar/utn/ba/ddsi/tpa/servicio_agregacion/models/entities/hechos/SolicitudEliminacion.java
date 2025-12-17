package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos;


import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.EstadoHecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.EstadoSolicitud;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class SolicitudEliminacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hecho", referencedColumnName = "id")
    private Hecho hechoAEliminar;
    @Column(length = 750)
    private String descripcion;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado;
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
    @Column
    private LocalDateTime fechaResolucion;
    @Column
    private String nombreAdministrador;
    @Column
    private String nombreUsuario;
    @Column
    private Boolean esSpam;

    public SolicitudEliminacion(Hecho hechoAEliminar, String descripcion) {
        if (descripcion == null || descripcion.length() <= 499) {
            throw new IllegalArgumentException("La descripcion debe tener al menos 500 caracteres");
        }

        this.hechoAEliminar = hechoAEliminar;
        this.descripcion = descripcion;
        this.estado = EstadoSolicitud.PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
    }

    public SolicitudEliminacion() {

    }

    public void rechazar() {
        this.estado = EstadoSolicitud.RECHAZADA;
        this.fechaResolucion = LocalDateTime.now();
    }

    public void aceptar() {
        this.estado = EstadoSolicitud.ACEPTADA;
        this.fechaResolucion = LocalDateTime.now();
    }

}
