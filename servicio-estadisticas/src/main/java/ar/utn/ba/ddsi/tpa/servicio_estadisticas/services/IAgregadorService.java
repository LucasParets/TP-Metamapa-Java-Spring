package ar.utn.ba.ddsi.tpa.servicio_estadisticas.services;

import reactor.core.publisher.Mono;

public interface IAgregadorService {
    void traerHechosDelAgregador();
    void traerColeccionesDelAgregador();
    void traerSolicitudesDelAgregador();
}
