package ar.utn.ba.ddsi.tpa.fuente_estatica.controllers;

import ar.utn.ba.ddsi.tpa.fuente_estatica.models.dtos.input.EliminarHechoInputDTO;
import ar.utn.ba.ddsi.tpa.fuente_estatica.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.tpa.fuente_estatica.services.IHechosService;
import ar.utn.ba.ddsi.tpa.fuente_estatica.services.impl.DatasetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/estatica")
public class FuenteController {
    private final IHechosService hechosService;
    private final DatasetService datasetService;

    public FuenteController(IHechosService hechosService, DatasetService datasetService) {
        this.hechosService = hechosService;
        this.datasetService = datasetService;
    }

    @PostMapping("/importar")
    public ResponseEntity<Void> importarDataset(@RequestParam("dataset") MultipartFile dataset) throws IOException {
        Path archivoGuardado = datasetService.guardarDataset(dataset);
        datasetService.importarDatasetDesdeArchivo(archivoGuardado);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/hechos")
    public ResponseEntity<List<HechoOutputDTO>> mostrarHechos(@RequestParam(required = false) String last_update) {
        return ResponseEntity.ok(hechosService.mostrarHechos(last_update));
    }

    @DeleteMapping("/hechos/{id}")
    public ResponseEntity<Void> eliminarHecho(@PathVariable Long id) {
        hechosService.eliminarHecho(id);
        return ResponseEntity.ok().build();
    }


}
