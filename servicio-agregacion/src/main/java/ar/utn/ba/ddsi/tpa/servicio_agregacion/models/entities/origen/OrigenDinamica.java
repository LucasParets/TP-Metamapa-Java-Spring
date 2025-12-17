package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.fuentes.TipoFuente;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("DINAMICA")
@Getter
@Setter
public class OrigenDinamica extends OrigenDelHecho {
    @Column
    private String nombreUsuario;

    @Column
    private boolean estaRegistrado;

    public OrigenDinamica(String nombreUsuario) {
        super(TipoFuente.DINAMICA);
        this.estaRegistrado = nombreUsuario != null;
        this.nombreUsuario = nombreUsuario;
    }

    public OrigenDinamica() {
        super(TipoFuente.DINAMICA);
        this.estaRegistrado = false;
    }

    public String getNombreOrigen() {
        return this.nombreUsuario != null ? this.nombreUsuario : null;
    }
}
