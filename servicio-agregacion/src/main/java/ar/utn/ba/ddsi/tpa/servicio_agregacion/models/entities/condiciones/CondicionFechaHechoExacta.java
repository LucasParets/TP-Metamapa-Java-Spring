package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.condiciones;


import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DiscriminatorValue("FechaHechoExacta")
public class CondicionFechaHechoExacta extends Condicion {
    @Column(name = "fecha_1")
    private LocalDate fecha;

    public CondicionFechaHechoExacta(LocalDateTime fecha) {
        this.fecha = fecha.toLocalDate();
    }

    public CondicionFechaHechoExacta() {
        super();
    }

    @Override
    public Specification<Hecho> cumple() {
        return (root, cq, cb) -> cb.and(
                cb.equal(root.get("fechaHecho"), this.fecha)
        );
    }
}
