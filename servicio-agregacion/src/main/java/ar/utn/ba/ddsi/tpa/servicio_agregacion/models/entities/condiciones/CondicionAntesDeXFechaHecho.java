package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.condiciones;


import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DiscriminatorValue("AntesXFechaHecho")
public class CondicionAntesDeXFechaHecho extends Condicion {
    @Column(name = "fecha_1")
    private LocalDateTime fechaLimite;

    public CondicionAntesDeXFechaHecho(LocalDateTime fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public CondicionAntesDeXFechaHecho() {
        super();
    }

    @Override
    public Specification<Hecho> cumple() {
        return (root, cq, cb) -> cb.and(
                cb.lessThanOrEqualTo(root.get("fechaHecho"), this.fechaLimite)
        );
    }
}
