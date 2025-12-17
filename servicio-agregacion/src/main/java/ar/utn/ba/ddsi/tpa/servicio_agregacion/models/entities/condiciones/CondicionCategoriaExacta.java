package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.condiciones;


import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Categoria;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

@Entity
@Getter
@Setter
@DiscriminatorValue("CategoriaExacta")
public class CondicionCategoriaExacta extends Condicion {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public CondicionCategoriaExacta(Categoria categoria) {
        this.categoria = categoria;
    }

    public CondicionCategoriaExacta() {
        super();
    }

    @Override
    public Specification<Hecho> cumple() {
        return (root, cq, cb) -> cb.and(
                cb.equal(root.get("categoria"), this.categoria)
        );
    }
}
