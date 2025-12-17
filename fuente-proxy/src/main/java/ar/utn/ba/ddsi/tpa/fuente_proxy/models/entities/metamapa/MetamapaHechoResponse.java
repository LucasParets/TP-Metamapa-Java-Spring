package ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.metamapa;

import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.Categoria;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.Coordenada;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.Hecho;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MetamapaHechoResponse {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private String ubicacion;
    private LocalDateTime fecha_hecho;
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_modificacion;

    public Hecho convertirEnHecho() {
        Hecho hecho = new Hecho();
        hecho.setId(this.id);
        hecho.setTitulo(this.titulo);
        hecho.setDescripcion(this.descripcion);
        hecho.setCategoria(new Categoria(this.categoria));
        hecho.setCoordenada(new Coordenada(this.latitud.floatValue(), this.longitud.floatValue()));
        hecho.setFecha_hecho(this.fecha_hecho);
        hecho.setFecha_creacion(this.fecha_creacion);
        hecho.setFecha_modificacion(this.fecha_modificacion);
        return hecho;
    }
}