package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Provincia {
    @Id
    private String id;
    @Column
    private String nombre;

    public Provincia(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Provincia() {
    }
}
