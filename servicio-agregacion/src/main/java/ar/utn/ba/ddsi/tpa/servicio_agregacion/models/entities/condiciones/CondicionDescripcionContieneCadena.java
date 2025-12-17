package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.condiciones;


import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

@Entity
@Getter
@Setter
@DiscriminatorValue("DescripcionContieneCadena")
public class CondicionDescripcionContieneCadena extends Condicion {
    @Column(name = "cadena")
    private String cadena;

    public CondicionDescripcionContieneCadena(String cadena) {
        this.cadena = cadena;
    }

    public CondicionDescripcionContieneCadena() {
        super();
    }

    @Override
    public Specification<Hecho> cumple() {
        return (root, cq, cb) -> {
            String like = "%"+this.cadena.trim().toLowerCase()+"%";
            return cb.and(cb.like(cb.lower(root.get("descripcion")), like));
        };
    }
}
