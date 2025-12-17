package ar.utn.ba.ddsi.tpa.fuente_proxy.controladores;

import ar.utn.ba.ddsi.tpa.fuente_proxy.models.dtos.metamapa.HechoMetamapa;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.dtos.metamapa.ColeccionDTO;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.dtos.metamapa.FiltrosDTO;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.dtos.metamapa.SolicitudDTO;
import ar.utn.ba.ddsi.tpa.fuente_proxy.servicios.impl.MetaMapaService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/proxy/metamapa")
public class MetaMapaController {
    private final MetaMapaService metaMapaService;

    @GetMapping("/colecciones")
    public ResponseEntity<Page<ColeccionDTO>> colecciones(@PageableDefault Pageable pg) {
        return ResponseEntity.ok(metaMapaService.importarColecciones(pg));
    }

    @GetMapping("/colecciones/{handle}")
    public ResponseEntity<ColeccionDTO> coleccion(@PathVariable String handle,
                                                  @RequestParam String metamapa){
        return ResponseEntity.ok(metaMapaService.importarColeccion(handle, metamapa));
    }

    @GetMapping("/colecciones/{handle}/hechos")
    public ResponseEntity<Page<HechoMetamapa>> hechosDeColeccion(@PageableDefault Pageable pg,
                                                                 @PathVariable String handle,
                                                                 @RequestParam String metamapa,
                                                                 @ModelAttribute FiltrosDTO f) {
        return ResponseEntity.ok(metaMapaService.importarHechosDeColeccion(handle, metamapa, pg, f));
    }

    @GetMapping("/hechos/{id}")
    public ResponseEntity<HechoMetamapa> hecho(@PathVariable Long id, @RequestParam String metamapa){
        return ResponseEntity.ok(metaMapaService.getHecho(id, metamapa));
    }

    @PostMapping("/solicitudes")
    public ResponseEntity<Void> solicitarEliminacionHecho(@RequestBody SolicitudDTO dto, @RequestParam String metamapa){
        metaMapaService.crearSolicitud(dto, metamapa);
        return ResponseEntity.ok().build();
    }

}