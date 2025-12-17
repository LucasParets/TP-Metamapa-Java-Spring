package ar.utn.ba.ddsi.tpa.servicio_agregacion.controllers;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.CriterioInputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.FiltroColecciones;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.CriterioOutputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.SolicitudEliminacionOutputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.dashboard.DashboardDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Categoria;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Coleccion;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Etiqueta;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.EstadoSolicitud;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.IColeccionService;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.IHechosService;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.ISolicitudEliminacionService;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.impl.DashboardService;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.impl.HechosService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agregador/admin")
public class ApiAdministrativaController {
    private final IColeccionService coleccionService;
    private final ISolicitudEliminacionService solicitudEliminacionService;
    private final DashboardService dashboardService;
    private final IHechosService hechosService;

    public ApiAdministrativaController(IColeccionService coleccionService,
                                       ISolicitudEliminacionService solicitudEliminacionService,
                                       DashboardService dashboardService, IHechosService hechosService) {
        this.coleccionService = coleccionService;
        this.solicitudEliminacionService = solicitudEliminacionService;
        this.dashboardService = dashboardService;
        this.hechosService = hechosService;
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

    @PostMapping("/colecciones")
    public ResponseEntity<Void> crearColeccion(@RequestBody ColeccionInputDTO dto) {
        String h = coleccionService.crearColeccion(dto);
        coleccionService.refrescarColeccion(h);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/colecciones/{handle}")
    public ResponseEntity<ColeccionOutputDTO> eliminarColeccion(@PathVariable String handle) {
        coleccionService.eliminarColeccion(handle);
        return ResponseEntity.ok().build() ;
    }

    @PatchMapping("/colecciones/{handle}")
    public ResponseEntity<Void> modificarColeccion(@PathVariable String handle, @RequestBody ColeccionInputDTO dto) {
        String h = coleccionService.modificarColeccion(handle, dto);
        if (h != null) coleccionService.refrescarColeccion(h);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/colecciones/{handle}/hechos")
    public ResponseEntity<List<HechoOutputDTO>> mostrarHechosDeColeccion(@PathVariable String handle) {
        return ResponseEntity.ok(coleccionService.mostrarHechos(handle));
    }

    @GetMapping("/colecciones/{handle}/criterios")
    public ResponseEntity<List<CriterioOutputDTO>> mostrarCriterios(@PathVariable String handle) {
        return ResponseEntity.ok(coleccionService.mostrarCriterios(handle));
    }

    @PostMapping("/colecciones/{handle}/criterios")
    public ResponseEntity<Void> agregarCriterio(@PathVariable String handle, @RequestBody CriterioInputDTO dto) {
        String h = coleccionService.agregarCriterio(handle, dto);
        coleccionService.refrescarColeccion(h);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/colecciones/{handle}/criterios/{id}")
    public ResponseEntity<Void> quitarCriterio(@PathVariable String handle, @PathVariable Long id) {
        String h = coleccionService.quitarCriterio(handle, id);
        coleccionService.refrescarColeccion(h);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/solicitudes")
    public ResponseEntity<Page<SolicitudEliminacionOutputDTO>> getSolicitudes(@PageableDefault Pageable pg,
                                                                              @RequestParam(required = false) EstadoSolicitud estado) {
        return ResponseEntity.ok(solicitudEliminacionService.mostrarSolicitudesDeEliminacion(pg, estado));
    }

    @PatchMapping("/solicitudes/{idSolicitud}/aprobar")
    public ResponseEntity<Void> aprobarSolicitud(@PathVariable Long idSolicitud, @RequestBody String admin) {
        solicitudEliminacionService.aceptarSolicitudDeEliminacion(idSolicitud, admin);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/solicitudes/{idSolicitud}/rechazar")
    public ResponseEntity<Void> rechazarSolicitud(@PathVariable Long idSolicitud, @RequestBody String admin) {
        solicitudEliminacionService.rechazarSolicitudDeEliminacion(idSolicitud, admin);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> mostrarDashboard() {
        return ResponseEntity.ok(this.dashboardService.generarDashboard());
    }

    @PostMapping("/hechos/{id}/etiquetas")
    public ResponseEntity<Void> agregarEtiqueta(@PathVariable Long id, @RequestBody String etiqueta) {
        hechosService.agregarEtiquetaAHecho(etiqueta, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/hechos/{idHecho}/etiquetas/{etiqueta}")
    public ResponseEntity<Void> eliminarEtiqueta(@PathVariable Long idHecho, @PathVariable String etiqueta) {
        hechosService.eliminarEtiquetaAHecho(etiqueta, idHecho);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/hechos/{id}/etiquetas/")
    public ResponseEntity<List<Etiqueta>> mostrarEtiquetas(@PathVariable Long id) {
        return ResponseEntity.ok(hechosService.mostrarEtiquetasDeHecho(id));
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> mostrarCategorias(@RequestParam(required = false) String handleColeccion) {
        return ResponseEntity.ok(hechosService.mostrarCategorias(handleColeccion));
    }

    @PostMapping("/colecciones/{handle}/destacada")
    public ResponseEntity<Void> destacarColeccion(@PathVariable String handle) {
        coleccionService.destacarColeccion(handle);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/colecciones/{handle}/destacada")
    public ResponseEntity<Void> sacarDestacadoColeccion(@PathVariable String handle) {
        coleccionService.quitarDestacado(handle);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/hechos/{id}")
    public ResponseEntity<Void> eliminarHecho(@PathVariable Long id, Authentication auth) {
        hechosService.eliminarHecho(id, auth.getName());
        return ResponseEntity.ok().build();
    }
}
