package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Etiqueta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String nombre;

    public Etiqueta(String nombre) {
        this.nombre = nombre;
    }

    public Etiqueta() {

    }
}
