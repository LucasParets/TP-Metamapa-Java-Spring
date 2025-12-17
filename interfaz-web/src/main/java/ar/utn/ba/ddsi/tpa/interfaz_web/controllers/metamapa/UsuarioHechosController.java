package ar.utn.ba.ddsi.tpa.interfaz_web.controllers.metamapa;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.CategoriaDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.HechoDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.PageResponse;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dinamica.HechoDinamicaDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input.HechoInputDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input.SolicitudInputDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.usuario.HechoUsuarioDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.exceptions.NotFoundException;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.DinamicaApiService;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.MetaMapaApiService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mis-hechos")
@AllArgsConstructor
public class UsuarioHechosController {
    private final DinamicaApiService dinamicaService;
    private final MetaMapaApiService metaMapaApiService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public String getMisHechos(@PageableDefault Pageable pg,
                               Authentication auth,
                               Model model) {
        if (!auth.isAuthenticated()) {
            return "redirect:/login";
        }
        try {
            PageResponse<HechoUsuarioDTO> hechosPage = dinamicaService.verHechosUsuario(pg);
            List<HechoUsuarioDTO> hechos = new ArrayList<>(hechosPage.getContent());
            model.addAttribute("hechos", hechos);
            model.addAttribute("titulo", "Mis Hechos");
            model.addAttribute("fechaLimite", LocalDateTime.now().minusDays(7));
            model.addAttribute("cantHechos", hechosPage.getTotalElements());
            model.addAttribute("totalPages", hechosPage.getTotalPages());
            model.addAttribute("currentPage", hechosPage.getNumber());
            model.addAttribute("pageFirst", hechosPage.isFirst());
            model.addAttribute("pageLast", hechosPage.isLast());
            return "metamapa/mis-hechos";
        }
        catch (Exception e) {
            model.addAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudieron traer los hechos.",
                    "confirmButtonText", "Aceptar"
            ));
            model.addAttribute("titulo", "Mis Hechos");
            model.addAttribute("hechos", List.of());
            return "metamapa/mis-hechos";
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER')")
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
            return "redirect:/mis-hechos";
        }
    }

    @GetMapping("/{id}/editar")
    @PreAuthorize("hasAnyRole('USER')")
    public String editarHecho(@PathVariable Long id, Model model, RedirectAttributes ra,
                              Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }
        try {
            HechoDinamicaDTO hecho = dinamicaService.getHecho(id);
            if (hecho.getFechaCarga().isBefore(LocalDateTime.now().minusDays(7))){
                return "redirect:/mis-hechos";
            }
            List<CategoriaDTO> categorias = metaMapaApiService.getCategorias(null);

            model.addAttribute("hecho", hecho);
            model.addAttribute("categorias", categorias);
            model.addAttribute("titulo", "Editar Hecho");

            return "metamapa/mapa/editar_hecho";
        }
        catch (Exception e) {
            ra.addAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "Error al cargar la página para editar el hecho.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/mis-hechos";
        }
    }

    @PostMapping("/{id}/actualizar")
    @PreAuthorize("hasAnyRole('USER')")
    public String actualizarHecho(@PathVariable Long id,
                                  @ModelAttribute HechoInputDTO hecho,
                                  RedirectAttributes ra,
                                  Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }
        try {
            hecho.setEstaRegistrado(true);
            hecho.setNombreUsuario(auth.getName());
            dinamicaService.actualizarHecho(id, hecho);
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "El hecho se actualizó correctamente.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/mis-hechos";
        }
        catch (Exception e) {
            ra.addAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "Error al tratar de actualizar el hecho.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/mis-hechos/" + id + "/editar";
        }
    }
}
