package ar.utn.ba.ddsi.tpa.fuente_dinamica.controllers;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.input.RevisionHechoInputDTO;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.output.RevisionOutputDTO;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.revision.EstadoRevision;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.services.IHechosService;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.services.impl.HechosService;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.services.impl.RevisionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dinamica/admin/revisiones")
public class RevisionController {
    private final RevisionService revisionService;

    public RevisionController(RevisionService revisionService) {
        this.revisionService = revisionService;
    }

    @PostMapping("/{id}/resolver")
    public ResponseEntity<Void> realizarRevision(@PathVariable Long id, @RequestBody RevisionHechoInputDTO revisionHechoInputDTO) {
        revisionService.revisarHecho(id, revisionHechoInputDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<RevisionOutputDTO>> mostrarRevisiones(@PageableDefault Pageable pg,
                                                                     @RequestParam(required = false) EstadoRevision estado) {
        return ResponseEntity.ok(revisionService.mostrarRevisiones(pg, estado));
    }
}
