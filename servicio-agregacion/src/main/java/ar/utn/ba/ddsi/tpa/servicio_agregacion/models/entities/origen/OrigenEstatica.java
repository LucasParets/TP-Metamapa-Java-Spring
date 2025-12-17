package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.fuentes.TipoFuente;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("ESTATICA")
public class OrigenEstatica extends OrigenDelHecho {
    @Column
    private String nombreArchivo;

    public OrigenEstatica(String nombreArchivo) {
        super(TipoFuente.ESTATICA);
        this.nombreArchivo = nombreArchivo;
    }

    public OrigenEstatica() {
        super(TipoFuente.ESTATICA);
    }

    public String getNombreOrigen() {
        return this.nombreArchivo;
    }
}
