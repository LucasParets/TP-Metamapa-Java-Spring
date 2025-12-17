package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.fuentes.TipoFuente;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo")
@Data
public abstract class OrigenDelHecho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private TipoFuente fuente;

    public OrigenDelHecho(TipoFuente fuente) {
        this.fuente = fuente;
    }

    public OrigenDelHecho() {

    }

    public abstract String getNombreOrigen();
}
