package ar.utn.ba.ddsi.tpa.fuente_dinamica.controllers;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.output.SolicitudModificacionDTO;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.EstadoSolicitud;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.services.impl.SolicitudesService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/dinamica/admin/solicitud-modificacion")
public class SolicitudController {
    private final SolicitudesService solicitudesService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<SolicitudModificacionDTO>> solicitudes(@PageableDefault Pageable pg,
                                                                      @RequestParam(required = false) EstadoSolicitud estado) {
        return ResponseEntity.ok(solicitudesService.getSolicitudes(pg, estado));
    }

    @PostMapping("/{id}/aceptar")
    @PreAuthorize("hasRole('ADMIN')")
    public void aceptarSolicitud(@PathVariable Long id, Authentication auth){
        solicitudesService.aceptarSolicitud(id, auth.getName());
    }

    @PostMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    public void rechazarSolicitud(@PathVariable Long id, Authentication auth){
        solicitudesService.rechazarSolicitud(id, auth.getName());
    }
}
