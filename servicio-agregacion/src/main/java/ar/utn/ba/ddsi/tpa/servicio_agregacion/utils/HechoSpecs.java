package ar.utn.ba.ddsi.tpa.servicio_agregacion.utils;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.BboxMapaDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.FiltroHechosMetamapa;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Coleccion;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HechoSpecs {
    public static Specification<Hecho> enColeccion(String handle){
        return (root, cq, cb) -> {

            Root<Coleccion> coleccion = Objects.requireNonNull(cq).from(Coleccion.class);
            Join<Coleccion, Hecho> hechos = coleccion.join("hechos");

            return cb.and(
                    cb.equal(coleccion.get("handle"), handle),
                    cb.equal(hechos.get("id"), root.get("id")),
                    cb.equal(hechos.get("estadoDelHecho"), "ACTIVO")
            );
        };
    }

    public static Specification<Hecho> enColeccionConsensuados(String handle){
        return (root, cq, cb) -> {

            Root<Coleccion> coleccion = Objects.requireNonNull(cq).from(Coleccion.class);
            Join<Coleccion, Hecho> hechos = coleccion.join("hechosConsensuados");

            return cb.and(
                    cb.equal(coleccion.get("handle"), handle),
                    cb.equal(hechos.get("id"), root.get("id"))
            );
        };
    }

    public static Specification<Hecho> filtros(FiltroHechosMetamapa f) {
        return (root, cq, cb) -> {

            List<Predicate> p = new ArrayList<>();

            p.add(cb.equal(root.get("estadoDelHecho"), "ACTIVO"));

            if (f.getQ() != null && !f.getQ().isBlank()) {
                String like = "%"+f.getQ().trim().toLowerCase()+"%";
                p.add(cb.like(cb.lower(root.get("titulo")), like));
            }

            if (f.getCategoria() != null && !f.getCategoria().isBlank()) {
                p.add(cb.equal(root.get("categoria").get("nombre"), f.getCategoria()));
            }

            if (f.getFecha_hecho_desde() != null)
                p.add(cb.greaterThanOrEqualTo(root.get("fechaHecho"), f.getFecha_hecho_desde()));

            if (f.getFecha_hecho_hasta() != null)
                p.add(cb.lessThanOrEqualTo(root.get("fechaHecho"), f.getFecha_hecho_hasta()));

            if (f.getFecha_carga_desde() != null)
                p.add(cb.greaterThanOrEqualTo(root.get("fechaCarga"), f.getFecha_carga_desde()));

            if (f.getFecha_carga_hasta() != null)
                p.add(cb.lessThanOrEqualTo(root.get("fechaCarga"), f.getFecha_carga_hasta()));

            return cb.and(p.toArray(new Predicate[0]));
        };
    }

    public static Specification<Hecho> bbox(BboxMapaDTO bbox) {
        return (root, cq, cb) -> cb.and(
                cb.between(root.get("coordenada").get("latitud"),  bbox.getS(), bbox.getN() ),
                cb.between(root.get("coordenada").get("longitud"), bbox.getW(),  bbox.getE())
        );
    }

    public static Specification<Hecho> repetidosEnOtrasFuentes(Long minRepetidos, String handle) {
        return (root, query, cb) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Hecho> subRoot = subquery.from(Hecho.class);

            Root<Coleccion> subColeccion = subquery.from(Coleccion.class);
            Join<Coleccion, Hecho> subHechos = subColeccion.join("hechos");

            double toleranciaDistancia = 0.5;
            int toleranciaFechaSegundos = 24 * 60 * 60;

            Expression<java.sql.Time> timeDiff = cb.function(
                    "TIMEDIFF",
                    java.sql.Time.class,
                    subRoot.get("fechaHecho"),
                    root.get("fechaHecho")
            );

            Expression<Integer> timeToSec = cb.function(
                    "TIME_TO_SEC",
                    Integer.class,
                    timeDiff
            );

            Expression<Double> difLatitud = cb.diff(
                    subRoot.get("coordenada").get("latitud"),
                    root.get("coordenada").get("latitud")
            );
            Expression<Double> difLongitud = cb.diff(
                    subRoot.get("coordenada").get("longitud"),
                    root.get("coordenada").get("longitud")
            );
            Expression<Double> distancia = cb.sqrt(
                    cb.sum(
                            cb.prod(difLatitud, difLatitud),
                            cb.prod(difLongitud, difLongitud)
                    )
            );

            subquery.select(cb.countDistinct(subRoot.get("origen")))
                    .where(
                            cb.and(
                                    cb.equal(subColeccion.get("handle"), handle),
                                    cb.equal(subHechos.get("id"), subRoot.get("id")),
                                    cb.equal(subHechos.get("estadoDelHecho"), "ACTIVO"),

                                    cb.equal(subRoot.get("titulo"), root.get("titulo")),
                                    cb.equal(subRoot.get("categoria"), root.get("categoria")),
                                    cb.lessThanOrEqualTo(cb.abs(timeToSec), toleranciaFechaSegundos),
                                    cb.lessThanOrEqualTo(distancia, toleranciaDistancia)
                            )
                    );

            return cb.greaterThanOrEqualTo(subquery, minRepetidos);
        };
    }

    public static Specification<Hecho> sinRepetidosQueNoCoinciden(String handle) {
        return (root, query, cb) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Hecho> subRoot = subquery.from(Hecho.class);

            double toleranciaDistancia = 0.5;
            int toleranciaFechaSegundos = 24*60*60;

            Root<Coleccion> subColeccion = subquery.from(Coleccion.class);
            Join<Coleccion, Hecho> subHechos = subColeccion.join("hechos");

            Expression<java.sql.Time> timeDiff = cb.function(
                    "TIMEDIFF",
                    java.sql.Time.class,
                    subRoot.get("fechaHecho"),
                    root.get("fechaHecho"));

            Expression<Integer> timeToSec = cb.function(
                    "TIME_TO_SEC",
                    Integer.class,
                    timeDiff);

            Expression<Double> difLatitud = cb.diff(subRoot.get("coordenada").get("latitud"), root.get("coordenada").get("latitud"));
            Expression<Double> difLongitud = cb.diff(subRoot.get("coordenada").get("longitud"), root.get("coordenada").get("longitud"));
            Expression<Double> distancia = cb.sqrt(
                    cb.sum(
                            cb.prod(difLatitud, difLatitud),
                            cb.prod(difLongitud, difLongitud)
                    )
            );

            subquery.select(cb.countDistinct(subRoot.get("origen")))
                    .where(
                            cb.and(
                                    cb.equal(subColeccion.get("handle"), handle),
                                    cb.equal(subHechos.get("id"), subRoot.get("id")),
                                    cb.equal(subHechos.get("estadoDelHecho"), "ACTIVO"),

                                    cb.equal(subRoot.get("titulo"), root.get("titulo")),
                                    cb.or(
                                            cb.notEqual(subRoot.get("categoria"), root.get("categoria")),
                                            cb.greaterThanOrEqualTo(cb.abs(timeToSec), toleranciaFechaSegundos),
                                            cb.greaterThanOrEqualTo(distancia, toleranciaDistancia)
                                    )
                            )
                    );

            return cb.equal(subquery, 0L);
        };
    }

}

