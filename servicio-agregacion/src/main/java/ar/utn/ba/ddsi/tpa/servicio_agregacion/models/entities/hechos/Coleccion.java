package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.condiciones.Condicion;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.consenso.*;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.fuentes.TipoFuente;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Setter
@Getter
public class Coleccion {
    @Id
    private String handle;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Convert(converter = AlgoritmoConsensoConverter.class)
    @Column(name = "algoritmo_consenso")
    private AlgoritmoConsenso algoritmoConsenso;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_coleccion", referencedColumnName = "handle")
    private Set<Condicion> criteriosDePertenencia;

    @ElementCollection
    @CollectionTable(
            name = "fuentes_permitidas",
            joinColumns = @JoinColumn(name = "coleccion_handle")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_fuente")
    private Set<TipoFuente> fuentesPermitidas;

    @ManyToMany
    @JoinTable(name = "HechosxColeccion",
            joinColumns = @JoinColumn(name = "id_coleccion", referencedColumnName = "handle"),
            inverseJoinColumns = @JoinColumn(name = "id_hecho", referencedColumnName = "id"))
    private List<Hecho> hechos;

    @ManyToMany
    @JoinTable(name = "Hechos_Consensuados",
            joinColumns = @JoinColumn(name = "id_coleccion", referencedColumnName = "handle"),
            inverseJoinColumns = @JoinColumn(name = "id_hecho", referencedColumnName = "id"))
    private List<Hecho> hechosConsensuados;

    @Column(nullable = false)
    private boolean esDestacado;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    public Coleccion(String titulo, String descripcion) {
        this.titulo = titulo;
        this.handle = generarHandle(titulo);
        this.descripcion = descripcion;
        this.criteriosDePertenencia = new HashSet<>();
        this.fuentesPermitidas = new HashSet<>();
        this.hechos = new ArrayList<>();
        this.hechosConsensuados = new ArrayList<>();
        this.esDestacado = false;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Coleccion() {

    }

    public void agregarHecho(Hecho hecho) {
        this.hechos.removeIf(h -> h.getId().equals(hecho.getId()));
        this.hechos.add(hecho);
    }

    private String generarHandle(String titulo) {
        return titulo
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
    }

    public void agregarHechos(List<Hecho> hechos) {
        this.hechos.addAll(hechos);
    }

//    public boolean sePuedeAgregarHecho(Hecho hecho) {
//        return criteriosDePertenencia.stream().allMatch(criterio -> criterio.cumple(hecho))
//                && this.fuentesPermitidas.contains(hecho.getOrigen().getFuente());
//    }

    public void agregarCriterio(Condicion criterioNuevo) {
        this.criteriosDePertenencia.add(criterioNuevo);
    }

    public void quitarCriterio(Condicion criterio) {
        this.criteriosDePertenencia.removeIf(c -> c.equals(criterio));
    }

    public void agregarFuentePermitida(String fuente) {
        TipoFuente tipoFuente = TipoFuente.valueOf(StringUtils.stripAccents(fuente.toUpperCase()));
        this.fuentesPermitidas.add(tipoFuente);
    }

    public void quitarFuentePermitida(TipoFuente fuente) {
        this.fuentesPermitidas.removeIf(f -> f.equals(fuente));
    }

    public void agregarConsenso(String algoritmo) {
        if (algoritmo == null || algoritmo.isBlank()) {
            this.algoritmoConsenso = null;
            return;
        }
        switch (algoritmo) {
            case "Absoluta" -> {
                this.algoritmoConsenso = new Absoluta();
                break;
            }
            case "MayoriaSimple" -> {
                this.algoritmoConsenso = new MayoriaSimple();
                break;
            }
            case "MultiplesMenciones" -> {
                this.algoritmoConsenso = new MultiplesMenciones();
                break;
            }
            default -> this.algoritmoConsenso = null;
        }
    }

    public void refrescarHechosConsensuados(List<Hecho> hechosConsensuados) {
        this.hechosConsensuados.clear();
        this.hechosConsensuados.addAll(hechosConsensuados);
    }

    public void refrescarHechos(List<Hecho> hechos) {
        this.hechos.clear();
        this.hechos.addAll(hechos);
    }

}
