package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.condiciones;


import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.Coordenada;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.criteria.Path;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

@Entity
@Getter
@Setter
@DiscriminatorValue("RangoDistancia")
public class CondicionRangoDistancia extends Condicion {
    @Column(name = "latitud")
    private float latitud;
    @Column(name = "longitud")
    private float longitud;
    @Column(name = "radio")
    private Double radioEnKm;

    public CondicionRangoDistancia(Coordenada coordenada, Double radioEnKm) {
        this.latitud = coordenada.getLatitud();
        this.longitud = coordenada.getLongitud();
        this.radioEnKm = radioEnKm;
    }

    public CondicionRangoDistancia() {
        super();
    }

    @Override
    public Specification<Hecho> cumple() {
        double dLat = radioEnKm / 110.574;
        double dLon = radioEnKm / (111.320 * Math.cos(Math.toRadians(latitud)));

        double minLat = latitud - dLat, maxLat = latitud + dLat;
        double minLon = longitud - dLon, maxLon = longitud + dLon;

        return (root, cq, cb) -> {
            Path<Double> lat = root.get("coordenada").get("latitud");
            Path<Double> lon = root.get("coordenada").get("longitud");
            return cb.and(
                    cb.isNotNull(lat), cb.isNotNull(lon),
                    cb.between(lat, minLat, maxLat),
                    cb.between(lon, minLon, maxLon)
            );
        };
    }
}
