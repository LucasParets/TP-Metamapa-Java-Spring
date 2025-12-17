package ar.utn.ba.ddsi.tpa.interfaz_web.controllers.admin;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.*;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.MetaMapaApiService;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.admin.EtiquetasService;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.admin.HechosService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@AllArgsConstructor
@RequestMapping("/admin/hechos")
public class HechosAdminController {
    private final EtiquetasService etiquetasService;
    private final HechosService hechosService;
    private final MetaMapaApiService metaMapaApiService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String getHechosDeColeccion(
                @ModelAttribute FiltrosHechos f,
                @PageableDefault(sort = "fechaHecho", direction= Sort.Direction.DESC) Pageable pg,
                Model model) {
        try {
            List<CategoriaDTO> categorias = metaMapaApiService.getCategorias(null);

            PageResponse<HechoDTO> hechos = hechosService.getHechos(f, pg);

            List<HechoDTO> listaHechos = new ArrayList<>(hechos.getContent());

            model.addAttribute("titulo", "Hechos");
            model.addAttribute("categorias", categorias);
            model.addAttribute("hechos", listaHechos);
            model.addAttribute("filtros", f);
            model.addAttribute("pg", pg);
            model.addAttribute("cantHechos", hechos.getTotalElements());
            model.addAttribute("totalPages", hechos.getTotalPages());
            model.addAttribute("currentPage", hechos.getNumber());
            model.addAttribute("pageFirst", hechos.isFirst());
            model.addAttribute("pageLast", hechos.isLast());

            return "admin/hecho/lista";
        }
        catch (Exception e) {
            model.addAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudieron obtener las colecciones.",
                    "confirmButtonText", "Aceptar"
            ));
            model.addAttribute("titulo", "Hechos");
            model.addAttribute("hechos", List.of());
            return "admin/hecho/lista";
        }
    }

    @PostMapping("/{id}/agregar-etiqueta")
    @PreAuthorize("hasRole('ADMIN')")
    public String agregarEtiqueta(@RequestParam String etiqueta, @PathVariable Long id, RedirectAttributes ra) {
        try {
            etiquetasService.agregarEtiqueta(id, etiqueta);
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "Se agregó correctamente la etiqueta.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/hechos";
        }
        catch (Exception e) {
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudo agregar la etiqueta." + e.getMessage() + e.getCause(),
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/hechos";
        }
    }

    @PostMapping("/{idHecho}/eliminar-etiqueta/{etiqueta}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminarEtiqueta(@PathVariable String etiqueta, @PathVariable Long idHecho, RedirectAttributes ra) {
        try {
            etiquetasService.eliminarEtiqueta(idHecho, etiqueta);
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "Se eliminó la etiqueta correctamente.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/hechos";
        }
        catch (Exception e) {
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudo eliminar la etiqueta." + e.getMessage() + e.getCause(),
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/hechos";
        }
    }

    @PostMapping("{id}/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            hechosService.eliminarHecho(id);
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "El hecho se eliminó correctamente.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/hechos";
        }
        catch (Exception ex) {
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "Error al tratar de eliminar el hecho.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/hechos";
        }
    }
}
