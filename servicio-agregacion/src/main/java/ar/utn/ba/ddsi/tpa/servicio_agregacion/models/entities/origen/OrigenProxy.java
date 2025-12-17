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
@DiscriminatorValue("PROXY")
public class OrigenProxy extends OrigenDelHecho {
    @Column
    private String nombreApi;

    public OrigenProxy(String nombreApi) {
        super(TipoFuente.PROXY);
        this.nombreApi = nombreApi;
    }

    public OrigenProxy() {
        super(TipoFuente.PROXY);
    }

    public String getNombreOrigen() {
        return this.nombreApi;
    }
}
