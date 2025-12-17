package ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String nombre;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public Categoria() {

    }
}
