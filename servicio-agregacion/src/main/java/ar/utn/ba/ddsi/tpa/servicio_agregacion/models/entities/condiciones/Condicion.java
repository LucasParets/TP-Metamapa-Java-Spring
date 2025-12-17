package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.condiciones;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

@Entity
@Table(name = "condicion")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo", discriminatorType = DiscriminatorType.STRING)
public abstract class Condicion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public abstract Specification<Hecho> cumple();
}
