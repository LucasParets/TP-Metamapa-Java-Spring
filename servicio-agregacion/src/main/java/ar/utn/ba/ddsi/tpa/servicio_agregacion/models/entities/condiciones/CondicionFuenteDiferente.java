package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.condiciones;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenDelHecho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

@Entity
@Getter
@Setter
@DiscriminatorValue("FuenteDiferente")
public class CondicionFuenteDiferente extends Condicion {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origen_id")
    private final OrigenDelHecho origen;

    public CondicionFuenteDiferente(OrigenDelHecho origen) {
        this.origen = origen;
    }

    public CondicionFuenteDiferente() {
        super();
        this.origen = null;
    }

    @Override
    public Specification<Hecho> cumple() {
        return (root, cq, cb) -> cb.and(
                cb.equal(root.get("origen"), this.origen)
        );
    }
}
