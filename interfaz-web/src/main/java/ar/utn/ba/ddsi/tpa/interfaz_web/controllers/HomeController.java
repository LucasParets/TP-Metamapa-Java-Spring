package ar.utn.ba.ddsi.tpa.interfaz_web.controllers;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.FiltrosHechos;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.HechoDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.PageResponse;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.inicio.ColeccionReducidaDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.inicio.DestacadosInicioDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.inicio.HechoReducidoDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.MetaMapaApiService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class HomeController {
    private final MetaMapaApiService metaMapaApiService;

    @GetMapping("/")
    public String home() {
        return "redirect:/inicio";
    }

    @GetMapping("/inicio")
    public String inicio(Model model) {
        try {
            DestacadosInicioDTO dto = metaMapaApiService.getDestacadosInicio();
            List<HechoReducidoDTO> hechosDestacados = dto.getHechosDestacados();
            List<ColeccionReducidaDTO> coleccionesDestacadas = dto.getColeccionesDestacadas();
            model.addAttribute("titulo", "Inicio");
            model.addAttribute("hechosDestacados", hechosDestacados);
            model.addAttribute("coleccionesDestacadas", coleccionesDestacadas);
            return "metamapa/inicio";
        }
        catch (Exception e) {
            model.addAttribute("titulo", "Inicio");
            model.addAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudieron traer los hechos destacados y colecciones destacadas. " + e.getMessage() + e.getCause(),
                    "confirmButtonText", "Aceptar"
            ));
            model.addAttribute("hechosDestacados", List.of());
            model.addAttribute("coleccionesDestacadas", List.of());
            return "metamapa/inicio";
        }
    }

}
