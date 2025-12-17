package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Coordenada {
    @Column(nullable = false)
    private float latitud;
    @Column(nullable = false)
    private float longitud;

    @Transient
    private final Integer radio_terrestre = 6371;

    public Coordenada(float latitud, float longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Coordenada() {

    }

    double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    public Double calcularDistancia(Coordenada coordenada){
        double dLat = Math.toRadians((coordenada.latitud - this.latitud));
        double dLong = Math.toRadians((coordenada.longitud - this.longitud));

        double startLat = Math.toRadians(this.latitud);
        double endLat = Math.toRadians(coordenada.latitud);

        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return radio_terrestre * c;
    }

}
