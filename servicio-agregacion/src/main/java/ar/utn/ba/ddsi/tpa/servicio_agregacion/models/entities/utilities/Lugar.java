package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Setter @Getter
public class Lugar {
    @Column
    private String nombre;
    @Embedded
    private Coordenada coordenada;

    public Lugar(String nombre, Coordenada coordenada) {
        this.nombre = nombre;
        this.coordenada = coordenada;
    }

    public Lugar(Coordenada coordenada) {
        this.coordenada = coordenada;
    }

    public Lugar() {

    }
}
