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
@DiscriminatorValue("IntervaloFechaCarga")
public class CondicionIntervaloDeFechasCarga extends Condicion {
    @Column(name = "fecha_1")
    private LocalDateTime fechaInicio;
    @Column(name = "fecha_2")
    private LocalDateTime fechaFin;

    public CondicionIntervaloDeFechasCarga(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public CondicionIntervaloDeFechasCarga() {
        super();
    }

    @Override
    public Specification<Hecho> cumple() {
        return (root, cq, cb) -> cb.and(
                cb.greaterThanOrEqualTo(root.get("fechaCarga"), this.fechaInicio),
                cb.lessThanOrEqualTo(root.get("fechaCarga"), this.fechaFin)
        );
    }
}
