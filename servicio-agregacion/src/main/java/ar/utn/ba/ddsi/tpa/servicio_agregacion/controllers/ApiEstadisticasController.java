package ar.utn.ba.ddsi.tpa.servicio_agregacion.controllers;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.estadisticas.ColeccionEstadisticas;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.estadisticas.HechoEstadisticas;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.estadisticas.SolicitudEstadisticas;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.impl.EstadisticasService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/agregador/estadisticas")
public class ApiEstadisticasController {
    private final EstadisticasService estadisticasService;

    @GetMapping("/hechos")
    public ResponseEntity<List<HechoEstadisticas>> mostrarHechos(@RequestParam String lastUpdate){
        return ResponseEntity.ok(estadisticasService.mostrarHechos(lastUpdate));
    }

    @GetMapping("/colecciones")
    public ResponseEntity<List<ColeccionEstadisticas>> mostrarColecciones(){
        return ResponseEntity.ok(estadisticasService.mostrarColecciones());
    }

    @GetMapping("/solicitudes")
    public ResponseEntity<List<SolicitudEstadisticas>> mostrarSolicitudes(@RequestParam String lastUpdate){
        return ResponseEntity.ok(estadisticasService.mostrarSolicitudes(lastUpdate));
    }
}
