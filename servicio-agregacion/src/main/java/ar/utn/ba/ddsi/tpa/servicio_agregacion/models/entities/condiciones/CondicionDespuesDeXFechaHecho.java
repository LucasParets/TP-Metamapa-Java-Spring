package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.condiciones;


import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DiscriminatorValue("DespuesDeXFechaHecho")
public class CondicionDespuesDeXFechaHecho extends Condicion {
    @Column(name = "fecha_1")
    private LocalDateTime fechaLimite;

    public CondicionDespuesDeXFechaHecho(LocalDateTime fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public CondicionDespuesDeXFechaHecho() {
        super();
    }

    @Override
    public Specification<Hecho> cumple() {
        return (root, cq, cb) -> cb.and(
                cb.greaterThanOrEqualTo(root.get("fechaHecho"), this.fechaLimite)
        );
    }
}
