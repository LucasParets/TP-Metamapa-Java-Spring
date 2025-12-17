package ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.utilities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Coordenada {
    @Column(nullable = false)
    private float latitud;
    @Column(nullable = false)
    private float longitud;

    public Coordenada(float latitud, float longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Coordenada() {

    }
}
