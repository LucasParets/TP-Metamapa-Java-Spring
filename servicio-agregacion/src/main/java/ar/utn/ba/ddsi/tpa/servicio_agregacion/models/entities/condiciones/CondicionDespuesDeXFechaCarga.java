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
@DiscriminatorValue("DespuesDeXFechaCarga")
public class CondicionDespuesDeXFechaCarga extends Condicion {
    @Column(name = "fecha_1")
    private LocalDateTime fechaLimite;

    public CondicionDespuesDeXFechaCarga(LocalDateTime fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public CondicionDespuesDeXFechaCarga() {
        super();
    }

    @Override
    public Specification<Hecho> cumple() {
        return (root, cq, cb) -> cb.and(
                cb.greaterThanOrEqualTo(root.get("fechaCarga"), this.fechaLimite)
        );
    }
}
