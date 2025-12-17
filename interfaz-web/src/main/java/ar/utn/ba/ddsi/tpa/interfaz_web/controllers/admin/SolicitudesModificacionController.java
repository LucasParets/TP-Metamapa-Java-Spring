package ar.utn.ba.ddsi.tpa.interfaz_web.controllers.admin;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.EstadoSolicitud;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.PageResponse;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dinamica.HechoDinamicaDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dinamica.SolicitudModificacionDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.DinamicaApiService;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.admin.SolicitudModificacionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/solicitudes-modificacion")
@AllArgsConstructor
public class SolicitudesModificacionController {
    private final SolicitudModificacionService solicitudesService;
    private final DinamicaApiService dinamicaService;

    @GetMapping
    public String solicitudesModificacion(@PageableDefault Pageable pg,
                                         @RequestParam(required = false) EstadoSolicitud estado,
                                         Model model) {
        try {
            PageResponse<SolicitudModificacionDTO> solicitudesPage = solicitudesService.getSolicitudes(pg, estado);
            List<SolicitudModificacionDTO> solicitudes = new ArrayList<>(solicitudesPage.getContent());
            model.addAttribute("solicitudes", solicitudes);
            model.addAttribute("estado", estado == null ? "TODOS" : estado.name());
            model.addAttribute("titulo", "Solicitudes de Modificación");
            model.addAttribute("cantHechos", solicitudesPage.getTotalElements());
            model.addAttribute("totalPages", solicitudesPage.getTotalPages());
            model.addAttribute("currentPage", solicitudesPage.getNumber());
            model.addAttribute("pageFirst", solicitudesPage.isFirst());
            model.addAttribute("pageLast", solicitudesPage.isLast());
            return "admin/solicitudes-modificacion";
        }
        catch (Exception e) {
            model.addAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudieron obtener las solicitudes." + e.getMessage() + e.getCause(),
                    "confirmButtonText", "Aceptar"
            ));
            model.addAttribute("titulo", "Solicitudes de Modificación");
            model.addAttribute("estado", estado == null ? "TODOS" : estado.name());
            return "admin/solicitudes-modificacion";
        }
    }

    @GetMapping("/hechos/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String verHecho(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            HechoDinamicaDTO hecho = dinamicaService.getHecho(id);
            hecho.setFuente("Dinámica");
            hecho.setOrigenHecho(hecho.getNombreUsuario());
            model.addAttribute("hechos", List.of(hecho));
            model.addAttribute("titulo", "Hecho");
            model.addAttribute("hecho", hecho);
            model.addAttribute("revision", true);
            return "metamapa/mapa/hecho";
        }
        catch (Exception e) {
            ra.addAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudo traer el hecho.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/solicitudes-modificacion";
        }
    }

    @PostMapping("/{id}/aceptar")
    @PreAuthorize("hasRole('ADMIN')")
    public String aprobarSolicitud(RedirectAttributes ra,
                                   Authentication auth,
                                   @PathVariable Long id) {
        try {
            String nombreAdmin = auth.getName();
            solicitudesService.aprobarSolicitud(id, nombreAdmin);
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "Se aprobo la solicitud y se ha modificado el hecho.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/solicitudes-modificacion";
        }
        catch (Exception e) {
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "Error al aprobar la solicitud." + e.getMessage() + e.getCause(),
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/solicitudes-modificacion";
        }
    }

    @PostMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    public String rechazarSolicitud(RedirectAttributes ra,
                                    Authentication auth,
                                    @PathVariable Long id) {
        try {
            String nombreAdmin = auth.getName();
            solicitudesService.rechazarSolicitud(id, nombreAdmin);
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "Se rechazo la solicitud de modificación.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/solicitudes-modificacion";
        }
        catch (Exception e) {
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "Error al rechazar la solicitud." + e.getMessage() + e.getCause(),
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/solicitudes-modificacion";
        }
    }
}
