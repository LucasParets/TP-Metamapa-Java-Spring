package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.condiciones;


import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Etiqueta;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import jakarta.persistence.*;
import jakarta.persistence.criteria.Join;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

@Entity
@Getter
@Setter
@DiscriminatorValue("ContieneEtiqueta")
public class CondicionContieneEtiqueta extends Condicion{
    @Column(name = "cadena")
    private String etiqueta;

    public CondicionContieneEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public CondicionContieneEtiqueta() {
        super();
    }

    @Override
    public Specification<Hecho> cumple() {
        return (root, cq, cb) -> {
            Join<Hecho, Etiqueta> etiquetas = root.join("etiquetas");
            return cb.and(cb.equal(etiquetas.get("nombre"), this.etiqueta));
        };
    }
}
