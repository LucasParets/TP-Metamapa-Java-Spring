package ar.utn.ba.ddsi.tpa.interfaz_web.controllers.admin;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.*;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.admin.NotificacionesApiService;
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
@RequestMapping("/admin/solicitudes-eliminacion")
@AllArgsConstructor
public class SolicitudesEliminacionController {
    private NotificacionesApiService solicitudesService;

    @GetMapping
    public String solicitudesEliminacion(@PageableDefault Pageable pg,
                                         @RequestParam(required = false) EstadoSolicitud estado,
                                         Model model) {
        try {
            PageResponse<SolicitudDTO> solicitudes = solicitudesService.getSolicitudesDeEliminacion(pg, estado);
            List<SolicitudDTO> listSolicitudes = new ArrayList<>(solicitudes.getContent());
            model.addAttribute("solicitudes", listSolicitudes);
            model.addAttribute("estado", estado == null ? "TODOS" : estado.name());
            model.addAttribute("titulo", "Solicitudes");
            model.addAttribute("cantHechos", solicitudes.getTotalElements());
            model.addAttribute("totalPages", solicitudes.getTotalPages());
            model.addAttribute("currentPage", solicitudes.getNumber());
            model.addAttribute("pageFirst", solicitudes.isFirst());
            model.addAttribute("pageLast", solicitudes.isLast());
            return "admin/solicitudes-eliminacion";
        }
        catch (Exception e) {
            model.addAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudieron obtener las solicitudes." + e.getMessage() + e.getCause(),
                    "confirmButtonText", "Aceptar"
            ));
            model.addAttribute("titulo", "Solicitudes");
            model.addAttribute("estado", estado == null ? "TODOS" : estado.name());
            return "admin/solicitudes-eliminacion";
        }
    }

    @PostMapping("/{id}/aprobar")
    @PreAuthorize("hasRole('ADMIN')")
    public String aprobarSolicitud(RedirectAttributes ra,
                                   Authentication auth,
                                   @PathVariable Long id) {
        try {
            String nombreAdmin = auth.getName();
            solicitudesService.aprobarSolicitudDeEliminacion(id, nombreAdmin);
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "Se aprobo la solicitud y se ha eliminado el hecho.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/solicitudes-eliminacion";
        }
        catch (Exception e) {
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "Error al aprobar la solicitud." + e.getMessage() + e.getCause(),
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/solicitudes-eliminacion";
        }
    }

    @PostMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    public String rechazarSolicitud(RedirectAttributes ra,
                                   Authentication auth,
                                   @PathVariable Long id) {
        try {
            String nombreAdmin = auth.getName();
            solicitudesService.rechazarSolicitudDeEliminacion(id, nombreAdmin);
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "Se rechazo la solicitud de eliminación.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/solicitudes-eliminacion";
        }
        catch (Exception e) {
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "Error al rechazar la solicitud." + e.getMessage() + e.getCause(),
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/solicitudes-eliminacion";
        }
    }
}
