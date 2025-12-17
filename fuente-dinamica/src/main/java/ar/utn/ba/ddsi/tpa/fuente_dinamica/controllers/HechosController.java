package ar.utn.ba.ddsi.tpa.fuente_dinamica.controllers;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.input.HechoInputDTO;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.input.RevisionHechoInputDTO;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.services.IHechosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dinamica/hechos")
public class HechosController {
    private final IHechosService hechosService;

    public HechosController(IHechosService hechosService) {
        this.hechosService = hechosService;
    }

    @PostMapping
    public ResponseEntity<Void> cargarHechos(@RequestBody HechoInputDTO hechoInputDTO) {
        hechosService.cargarHecho(hechoInputDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HechoOutputDTO> mostrarHecho(@PathVariable Long id) {
        return ResponseEntity.ok(hechosService.getHecho(id));
    }

    @GetMapping
    public ResponseEntity<List<HechoOutputDTO>> mostrarHechos(@RequestParam(required = false) String last_update) {
        return ResponseEntity.ok(hechosService.mostrarHechos(last_update));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HechoOutputDTO> modificarHecho(@PathVariable Long id, @RequestBody HechoInputDTO hechoInputDTO) {
        hechosService.modificarHecho(id, hechoInputDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHecho(@PathVariable Long id) {
        hechosService.eliminarHecho(id);
        return ResponseEntity.ok().build();
    }

}
