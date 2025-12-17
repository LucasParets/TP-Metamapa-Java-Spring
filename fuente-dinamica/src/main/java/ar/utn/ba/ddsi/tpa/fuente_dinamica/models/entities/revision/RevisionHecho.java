package ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.revision;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.utilities.EstadoHecho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class RevisionHecho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRevision estado;

    @Column(length = 1000)
    private String sugerencia;

    @Column
    private String usuarioAdmin;

    @Column
    private LocalDateTime fechaRevision;

    @OneToOne
    @JoinColumn(name = "id_hecho", referencedColumnName = "id")
    private Hecho hecho;

    public RevisionHecho() {
        this.estado = EstadoRevision.PENDIENTE;
    }

    public RevisionHecho(Hecho hecho) {
        this.hecho = hecho;
        this.estado = EstadoRevision.PENDIENTE;
    }

    public void resolverRevision(String usuarioAdmin, EstadoRevision estado, String sugerencia) {
        this.estado = estado;
        if (estado == EstadoRevision.ACEPTADO) {
            this.hecho.setEstadoDelHecho(EstadoHecho.ACTIVO);
            if (sugerencia != null) {
                this.sugerencia = sugerencia;
            }
        }
        if (estado == EstadoRevision.RECHAZADO) {
            this.hecho.setEstadoDelHecho(EstadoHecho.INACTIVO);
        }
        this.usuarioAdmin = usuarioAdmin;
        this.fechaRevision = LocalDateTime.now();
    }
}
