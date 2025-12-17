package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.spam;

import java.util.List;

public class DetectorDeSpamBasico implements DetectorDeSpam {
    private final List<String> palabrasSpam = List.of(
            "gratis", "dinero", "oferta", "promocion", "click aqui",
            "suscribite", "urgente", "ganar plata", "spam", "casino", "lorem ipsum"
    );

    @Override
    public boolean esSpam(String texto) {
        return palabrasSpam.stream().anyMatch(texto.toLowerCase()::contains);
    }
}
