package ar.utn.ba.ddsi.tpa.fuente_proxy.models.response;

import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.Categoria;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.Coordenada;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.Hecho;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Desastre {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private LocalDateTime fecha_hecho;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Hecho convertirEnHecho() {
        Hecho hecho = new Hecho();
        hecho.setIdExterno(this.id);
        hecho.setTitulo(this.titulo);
        hecho.setDescripcion(this.descripcion);
        hecho.setCategoria(new Categoria(this.categoria));
        hecho.setCoordenada(new Coordenada(this.latitud.floatValue(), this.longitud.floatValue()));
        hecho.setFecha_hecho(this.fecha_hecho);
        hecho.setFecha_creacion(this.created_at);
        hecho.setFecha_modificacion(this.updated_at);
        return hecho;
    }
}
