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
@DiscriminatorValue("TituloExacto")
public class CondicionTituloExacto extends Condicion {
    @Column(name = "cadena")
    private String titulo;

    public CondicionTituloExacto(String titulo) {
        this.titulo = titulo.trim().toLowerCase();
    }

    public CondicionTituloExacto() {
        super();
    }

    @Override
    public Specification<Hecho> cumple() {
        return (root, cq, cb) -> cb.and(
                cb.equal(root.get("titulo"), this.titulo)
        );
    }
}
