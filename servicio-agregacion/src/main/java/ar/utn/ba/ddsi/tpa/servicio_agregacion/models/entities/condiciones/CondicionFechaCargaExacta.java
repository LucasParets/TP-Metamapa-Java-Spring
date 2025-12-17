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
@DiscriminatorValue("FechaCargaExacta")
public class CondicionFechaCargaExacta extends Condicion {
    @Column(name = "fecha_1")
    private LocalDate fecha;

    public CondicionFechaCargaExacta() {
        super();
    }

    public CondicionFechaCargaExacta(LocalDateTime fecha) {
        this.fecha = fecha.toLocalDate();
    }

    @Override
    public Specification<Hecho> cumple() {
        return (root, cq, cb) -> cb.and(
                cb.equal(root.get("fechaCarga"), this.fecha)
        );
    }
}
