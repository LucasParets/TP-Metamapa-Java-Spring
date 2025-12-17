package ar.utn.ba.ddsi.tpa.servicio_agregacion.controllers;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.BboxMapaDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.FiltroColecciones;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.FiltroHechosMetamapa;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.destacados.DestacadosInicioDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Categoria;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.IColeccionService;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.IHechosService;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.ISolicitudEliminacionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/agregador/public")
public class ApiPublicaController {
    private final IHechosService hechosService;
    private final IColeccionService coleccionService;
    private final ISolicitudEliminacionService solicitudEliminacionService;

    @GetMapping("/me")
    public Map<String,Object> me(@AuthenticationPrincipal org.springframework.security.oauth2.jwt.Jwt jwt){
        return Map.of("sub", jwt.getSubject(), "roles", jwt.getClaimAsStringList("roles"));
    }

    @GetMapping("/hechos")
    public ResponseEntity<Page<HechoOutputDTO>> mostrarHechos(@ModelAttribute FiltroHechosMetamapa filtroHechos,
                                                              @PageableDefault Pageable pg) {
        return ResponseEntity.ok(hechosService.mostrarHechos(filtroHechos, pg));
    }

    @GetMapping("/hechos/{id}")
    public ResponseEntity<HechoOutputDTO> mostrarHecho(@PathVariable Long id) {
        return ResponseEntity.ok(hechosService.mostrarHecho(id));
    }

    @GetMapping("/colecciones")
    public ResponseEntity<Page<ColeccionOutputDTO>> mostrarColecciones(@PageableDefault Pageable pg,
                                                                       @ModelAttribute FiltroColecciones f) {
        return ResponseEntity.ok(coleccionService.mostrarColecciones(f, pg));
    }

    @GetMapping("/colecciones/{handle}")
    public ResponseEntity<ColeccionOutputDTO> mostrarColeccion(@PathVariable String handle) {
        return ResponseEntity.ok(coleccionService.mostrarColeccion(handle));
    }

    @GetMapping("/colecciones/{handle}/hechos")
    public ResponseEntity<Page<HechoOutputDTO>> mostrarHechosDeColeccion(@PathVariable String handle,
                                                                         @ModelAttribute FiltroHechosMetamapa filtroHechos,
                                                                         @PageableDefault Pageable pg) {
        return ResponseEntity.ok(hechosService.mostrarHechosDeUnaColeccion(handle, filtroHechos, pg));
    }

    @GetMapping("/hechos/mapa")
    public ResponseEntity<Map<String, Object>> mostrarHechosMapa(@RequestParam(required = false) String handle,
                                                                            @ModelAttribute FiltroHechosMetamapa filtroHechos,
                                                                            @ModelAttribute BboxMapaDTO bbox,
                                                                            @RequestParam int limit) {
        return ResponseEntity.ok(hechosService.mostrarHechosMapa(handle, filtroHechos, bbox, PageRequest.of(0, limit)));
    }



    @PostMapping("/solicitudes")
    public ResponseEntity<Void> crearSolicitudDeEliminacion(@RequestBody SolicitudEliminacionInputDTO dto) {
        solicitudEliminacionService.cargarSolicitudDeEliminacion(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> mostrarCategorias(@RequestParam(required = false) String handleColeccion) {
        return ResponseEntity.ok(hechosService.mostrarCategorias(handleColeccion));
    }

    @GetMapping("/inicio/destacados")
    public ResponseEntity<DestacadosInicioDTO> mostrarDestacados() {
        return ResponseEntity.ok(hechosService.mostrarDestacadosInicio());
    }

    @GetMapping("/hechos/destacados")
    public ResponseEntity<Page<HechoOutputDTO>> mostrarHechosDestacados(@PageableDefault(sort = "fechaCarga", direction = Sort.Direction.DESC) Pageable pg,
                                                                        @ModelAttribute FiltroHechosMetamapa f) {
        return ResponseEntity.ok(hechosService.mostrarHechosDestacados(f, pg).map(hechosService::hechoADTO));
    }
}
