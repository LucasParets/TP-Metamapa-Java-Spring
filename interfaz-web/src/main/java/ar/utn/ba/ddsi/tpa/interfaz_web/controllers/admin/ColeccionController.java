package ar.utn.ba.ddsi.tpa.interfaz_web.controllers.admin;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.CategoriaDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.ColeccionDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.PageResponse;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input.CriterioDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.exceptions.NotFoundException;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.MetaMapaApiService;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.admin.AdminColeccionApiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/colecciones")
public class ColeccionController {
    private final MetaMapaApiService metamapaApiService;
    private final AdminColeccionApiService adminColeccionApiService;

    public ColeccionController(MetaMapaApiService metamapaApiService, AdminColeccionApiService adminColeccionApiService) {
        this.metamapaApiService = metamapaApiService;
        this.adminColeccionApiService = adminColeccionApiService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String colecciones(@PageableDefault Pageable pg,
                              @RequestParam(required = false) String q,
                              Model model) {
        try {
            PageResponse<ColeccionDTO> coleccionesPage = adminColeccionApiService.getColecciones(q, pg);

            List<ColeccionDTO> colecciones = coleccionesPage.getContent();

            model.addAttribute("colecciones", colecciones);
            model.addAttribute("q", q);
            model.addAttribute("titulo", "Colecciones");
            model.addAttribute("cantHechos", coleccionesPage.getTotalElements());
            model.addAttribute("totalPages", coleccionesPage.getTotalPages());
            model.addAttribute("currentPage", coleccionesPage.getNumber());
            model.addAttribute("pageFirst", coleccionesPage.isFirst());
            model.addAttribute("pageLast", coleccionesPage.isLast());
            return "admin/coleccion/lista";
        }
        catch (Exception e) {
            model.addAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudieron obtener las colecciones.",
                    "confirmButtonText", "Aceptar"
            ));
            model.addAttribute("titulo", "Colecciones");
            model.addAttribute("colecciones", List.of());
            return "admin/coleccion/lista";
        }
    }

    @GetMapping("/crear")
    @PreAuthorize("hasRole('ADMIN')")
    public String crear(Model model) {
        model.addAttribute("titulo", "Crear Colección");
        model.addAttribute("coleccion", new ColeccionInputDTO());
        return "admin/coleccion/crear";
    }

    @PostMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String nuevo(@ModelAttribute ColeccionInputDTO coleccion,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        try {
            adminColeccionApiService.crearColeccion(coleccion);
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "Se creó la colección '" + coleccion.getTitulo() + "' correctamente.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/colecciones";
        }
        catch (Exception e) {
            model.addAttribute("titulo", "Crear Colección");
            model.addAttribute("coleccion", coleccion);
            model.addAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudo crear la colección.",
                    "confirmButtonText", "Aceptar"
            ));
            return "admin/coleccion/crear";
        }
    }

    @GetMapping("/{handle}/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public String editarColeccion(@PathVariable String handle, Model model, RedirectAttributes redirectAttributes) {
        try {
            ColeccionDTO coleccion = adminColeccionApiService.getColeccion(handle);
            model.addAttribute("coleccion", coleccion);
            model.addAttribute("titulo", "Editar coleccion");
            return "admin/coleccion/editar";
        }
        catch (NotFoundException ex) {
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudo encontrar la colección a editar.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/colecciones";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "Error al dirigirse a la página para editar colección.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/colecciones";
        }
    }

    @PostMapping("/{handle}/actualizar")
    @PreAuthorize("hasRole('ADMIN')")
    public String actualizarColeccion(@PathVariable String handle,
                                      @ModelAttribute ColeccionInputDTO dto,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        try {
            adminColeccionApiService.actualizarColeccion(handle, dto);
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "Se actualizo la colección '" + dto.getTitulo() + "' correctamente.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/colecciones";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "Error al modificar la colección.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/colecciones/" + handle + "/editar";
        }
    }

    @PostMapping("/{handle}/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminarColeccion(@PathVariable String handle, RedirectAttributes redirectAttributes) {
        try {
            adminColeccionApiService.eliminarColeccion(handle);
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "La colección se eliminó correctamente.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/colecciones";
        }
        catch (Exception ex) {
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "Error al tratar de eliminar la colección.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/colecciones";
        }
    }

    @GetMapping("/{handle}/criterios")
    @PreAuthorize("hasRole('ADMIN')")
    public String criterios(@PathVariable String handle,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        try {
            List<CriterioDTO> criterios = adminColeccionApiService.getCriteriosDeColeccion(handle);
            List<CategoriaDTO> categorias = metamapaApiService.getCategorias(null);
            boolean tieneCriterioDeFecha = false;
            for (CriterioDTO criterio : criterios) {
                if (criterio.getNombre_criterio().contains("fecha")) {
                    tieneCriterioDeFecha = true;
                    break;
                }
            }
            model.addAttribute("titulo", "Criterios");
            model.addAttribute("categorias", categorias);
            model.addAttribute("criterios", criterios);
            model.addAttribute("tieneCriterioDeFecha", tieneCriterioDeFecha);
            return "admin/coleccion/criterios";
        }
        catch (Exception ex) {
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "Error al cargar los criterios de la colección.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/colecciones";
        }
    }

    @PostMapping("/{handle}/criterios/agregar")
    @PreAuthorize("hasRole('ADMIN')")
    public String agregarCriterio(@PathVariable String handle, @ModelAttribute CriterioDTO dto, RedirectAttributes redirectAttributes) {
        try {
            adminColeccionApiService.agregarCriterioAColeccion(handle, dto);
            return "redirect:/admin/colecciones/" + handle + "/criterios";
        }
        catch (Exception ex) {
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "Error al intentar agregar un criterios.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/colecciones/" + handle + "/criterios";
        }
    }

    @PostMapping("/{handle}/criterios/{idCriterio}/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminarCriterio(@PathVariable String handle, @PathVariable Long idCriterio, RedirectAttributes redirectAttributes) {
        try {
            adminColeccionApiService.quitarCriterioAColeccion(handle, idCriterio);
            return "redirect:/admin/colecciones/" + handle + "/criterios";
        }
        catch (NotFoundException ex) {
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "Error al intentar eliminar un criterios.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/colecciones/" + handle + "/criterios";
        }
    }

    @PostMapping("/{handle}/agregar-destacado")
    @PreAuthorize("hasRole('ADMIN')")
    public String agregarDestacado(@PathVariable String handle,
                                   RedirectAttributes ra) {
        try {
            adminColeccionApiService.destacarColeccion(handle);
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "Se ha agregado la coleccion a destacados.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/colecciones";
        } catch (Exception e) {
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "Error al intentar destacar un colección. " + e.getMessage(),
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/colecciones";
        }
    }

    @PostMapping("/{handle}/quitar-destacado")
    @PreAuthorize("hasRole('ADMIN')")
    public String quitarDestacado(@PathVariable String handle,
                                   RedirectAttributes ra) {
        try {
            adminColeccionApiService.quitarDestacadoColeccion(handle);
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "La colección se ha quitado de destacados.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/colecciones";
        } catch (Exception e) {
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "Error al intentar quitar una colección de destacados.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/admin/colecciones";
        }
    }
}