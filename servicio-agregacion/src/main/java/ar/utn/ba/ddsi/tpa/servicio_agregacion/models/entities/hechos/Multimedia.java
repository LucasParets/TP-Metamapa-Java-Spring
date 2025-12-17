package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Multimedia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String tipo;
    @Column(nullable = false)
    private String publicId;

    public Multimedia(String url, String tipo, String publicId) {
        this.url = url;
        this.tipo = tipo;
        this.publicId = publicId;
    }

    public Multimedia() {

    }
}
