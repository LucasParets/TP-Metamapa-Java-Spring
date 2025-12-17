package ar.utn.ba.ddsi.tpa.interfaz_web.controllers.admin;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.*;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dinamica.HechoDinamicaDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dinamica.RevisionHechoInputDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.admin.NotificacionesApiService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/revisiones")
@AllArgsConstructor
public class RevisionesController {
    private final NotificacionesApiService revisionesService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String revisiones(@PageableDefault Pageable pg,
                             @RequestParam(required = false) EstadoRevision estado,
                             Model model) {
        try {
            PageResponse<RevisionDTO> revisiones = revisionesService.getRevisiones(pg, estado);
            List<RevisionDTO> listRevisiones = new ArrayList<>(revisiones.getContent());
            model.addAttribute("revisiones", listRevisiones);
            model.addAttribute("estado", estado == null ? "TODOS" : estado.name());
            model.addAttribute("titulo", "Revisiones");
            model.addAttribute("cantHechos", revisiones.getTotalElements());
            model.addAttribute("totalPages", revisiones.getTotalPages());
            model.addAttribute("currentPage", revisiones.getNumber());
            model.addAttribute("pageFirst", revisiones.isFirst());
            model.addAttribute("pageLast", revisiones.isLast());
            return "admin/revisiones";
        }
        catch (Exception e) {
            model.addAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudieron obtener las revisiones.",
                    "confirmButtonText", "Aceptar"
            ));
            model.addAttribute("titulo", "Revisiones");
            model.addAttribute("estado", estado == null ? "TODOS" : estado.name());
            model.addAttribute("revisiones", List.of());
            return "admin/revisiones";
        }
    }

    @GetMapping("/hecho/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String hechoARevisar(@PathVariable Long id, Model model) {
        try {
            HechoDinamicaDTO hecho = revisionesService.getHecho(id);
            hecho.setFuente("Dinámica");
            hecho.setOrigenHecho(hecho.getNombreUsuario());
            model.addAttribute("hechos", List.of(hecho));
            model.addAttribute("titulo", "Hecho");
            model.addAttribute("hecho", hecho);
            model.addAttribute("revision", true);
            return "metamapa/mapa/hecho";
        }
        catch (Exception e) {
            model.addAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudo traer el hecho.",
                    "confirmButtonText", "Aceptar"
            ));
            model.addAttribute("titulo", "Revisiones");
            model.addAttribute("estado", "TODOS");
            model.addAttribute("revisiones", List.of());
            return "admin/revisiones";
        }
    }

    @PostMapping("/{id}/resolver")
    @PreAuthorize("hasRole('ADMIN')")
    public String resolverRevision(@PathVariable Long id,
                                   @ModelAttribute RevisionHechoInputDTO dto,
                                   RedirectAttributes ra) {
        try {
            revisionesService.resolverRevision(id, dto);
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "La revisión se resolvió con éxito.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/revisiones";
        }
        catch (Exception e) {
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudo resolver la revisión." + e.getMessage() + e.getCause(),
                    "confirmButtonText", "Aceptar"
            ));
            ra.addFlashAttribute("titulo", "Revisiones");
            return "redirect:/admin/revisiones";
        }
    }

}
