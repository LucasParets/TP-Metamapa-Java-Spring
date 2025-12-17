package ar.utn.ba.ddsi.tpa.interfaz_web.controllers;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.ColeccionDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.FiltrosColeccion;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.PageResponse;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.MetaMapaApiService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MetaMapaController {
    private final MetaMapaApiService metaMapaApiService;

    public MetaMapaController(MetaMapaApiService metaMapaApiService) {
        this.metaMapaApiService = metaMapaApiService;
    }

    @GetMapping("/colecciones")
    public String getColecciones(@PageableDefault Pageable pg,
                                 @ModelAttribute FiltrosColeccion f,
                                 Model model) {
        try {
            PageResponse<ColeccionDTO> coleccionesPage = metaMapaApiService.getColecciones(f, pg);

            List<ColeccionDTO> colecciones = coleccionesPage.getContent();

            model.addAttribute("colecciones", colecciones);
            model.addAttribute("titulo", "Colecciones");
            model.addAttribute("filtros", f);
            model.addAttribute("cantHechos", coleccionesPage.getTotalElements());
            model.addAttribute("totalPages", coleccionesPage.getTotalPages());
            model.addAttribute("currentPage", coleccionesPage.getNumber());
            model.addAttribute("pageFirst", coleccionesPage.isFirst());
            model.addAttribute("pageLast", coleccionesPage.isLast());
            return "metamapa/colecciones";
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
            return "metamapa/colecciones";
        }
    }
}
