package ar.utn.ba.ddsi.tpa.interfaz_web.controllers.metamapa;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.*;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dinamica.MultimediaDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input.HechoInputDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input.SolicitudInputDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.DinamicaApiService;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.MetaMapaApiService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class HechosController {
    private final MetaMapaApiService metaMapaApiService;
    private final DinamicaApiService dinamicaApiService;
    private final String apiGatewayUrl;

    public HechosController(MetaMapaApiService metaMapaApiService,
                            DinamicaApiService dinamicaApiService,
                            @Value("${api-gateway-publico.url}") String apiGatewayUrl) {
        this.metaMapaApiService = metaMapaApiService;
        this.dinamicaApiService = dinamicaApiService;
        this.apiGatewayUrl = apiGatewayUrl;
    }

    @GetMapping("/colecciones/{handle}/hechos")
    public String getHechosDeColeccion(
            @PathVariable String handle,
            @ModelAttribute FiltrosHechos f,
            @PageableDefault(sort = "fechaHecho", direction= Sort.Direction.DESC) Pageable pg,
            Model model,
            RedirectAttributes ra) {

        try {
             ColeccionDTO coleccion = this.metaMapaApiService.getColeccion(handle);

             List<CategoriaDTO> categorias = this.metaMapaApiService.getCategorias(handle);

             PageResponse<HechoDTO> hechos = this.metaMapaApiService.getHechosDeColeccion(handle, f, pg);

             List<HechoDTO> listaHechos = new ArrayList<>(hechos.getContent());

            if (f.getModo_navegacion() == null) {
                f.setModo_navegacion("IRRESTRICTA");
            }

            model.addAttribute("coleccion", coleccion);
            model.addAttribute("categorias", categorias);
            model.addAttribute("mostrarConsenso", true);
            model.addAttribute("hechos", listaHechos);
            model.addAttribute("tieneHechos", !hechos.isEmpty());
            model.addAttribute("filtros", f);
            model.addAttribute("pg", pg);
            model.addAttribute("cantHechos", hechos.getTotalElements());
            model.addAttribute("totalPages", hechos.getTotalPages());
            model.addAttribute("currentPage", hechos.getNumber());
            model.addAttribute("pageFirst", hechos.isFirst());
            model.addAttribute("pageLast", hechos.isLast());
            model.addAttribute("titulo", "Hechos");
            model.addAttribute("api_gateway", apiGatewayUrl);
            return "metamapa/mapa/lista_hecho";
        }
        catch (Exception e) {
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudo traer los hechos: " + e.getMessage(),
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/colecciones";
        }
    }

    @GetMapping("/hechos")
    public String getHechos(
            @ModelAttribute FiltrosHechos f,
            @PageableDefault(sort = "fechaHecho", direction= Sort.Direction.DESC) Pageable pg,
            Model model,
            RedirectAttributes ra) {
        try {
            List<CategoriaDTO> categorias = this.metaMapaApiService.getCategorias(null);

            PageResponse<HechoDTO> hechos = this.metaMapaApiService.getHechos(f, pg);

            List<HechoDTO> listaHechos = new ArrayList<>(hechos.getContent());

            model.addAttribute("categorias", categorias);
            model.addAttribute("mostrarConsenso", false);
            model.addAttribute("hechos", listaHechos);
            model.addAttribute("tieneHechos", !hechos.isEmpty());
            model.addAttribute("filtros", f);
            model.addAttribute("pg", pg);
            model.addAttribute("cantHechos", hechos.getTotalElements());
            model.addAttribute("totalPages", hechos.getTotalPages());
            model.addAttribute("currentPage", hechos.getNumber());
            model.addAttribute("pageFirst", hechos.isFirst());
            model.addAttribute("pageLast", hechos.isLast());
            model.addAttribute("titulo", "Hechos");
            model.addAttribute("api_gateway", apiGatewayUrl);
            return "metamapa/mapa/lista_hecho";
        }
        catch (Exception e) {
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudo traer los hechos: " + e.getMessage(),
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/inicio";
        }
    }

    @GetMapping("/hechos-destacados")
    public String hechosDestacados(@PageableDefault Pageable pg,
                                   @ModelAttribute FiltrosHechos f,
                                   RedirectAttributes ra,
                                   Model model) {
        try {
            List<CategoriaDTO> categorias = this.metaMapaApiService.getCategorias(null);

            PageResponse<HechoDTO> hechoPage = metaMapaApiService.getHechosDestacados(f, pg);

            List<HechoDTO> hechos = hechoPage.getContent();

            model.addAttribute("titulo", "Hechos destacados");
            model.addAttribute("hechos", hechos);
            model.addAttribute("mostrarConsenso", false);
            model.addAttribute("tieneHechos", !hechoPage.isEmpty());
            model.addAttribute("categorias", categorias);
            model.addAttribute("filtros", f);
            model.addAttribute("pg", pg);
            model.addAttribute("cantHechos", hechoPage.getTotalElements());
            model.addAttribute("totalPages", hechoPage.getTotalPages());
            model.addAttribute("currentPage", hechoPage.getNumber());
            model.addAttribute("pageFirst", hechoPage.isFirst());
            model.addAttribute("pageLast", hechoPage.isLast());
            model.addAttribute("api_gateway", apiGatewayUrl);

            return "metamapa/mapa/lista_hecho";
        }
        catch (Exception e) {
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudieron traer los hechos destacados: " + e.getMessage(),
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/inicio";
        }
    }

    @GetMapping("/hechos/{idHecho}")
    public String getHecho(
            @PathVariable Long idHecho,
            Model model,
            RedirectAttributes ra) {
        try {
            HechoDTO hecho = this.metaMapaApiService.getHecho(idHecho);
            model.addAttribute("hechos", List.of(hecho));
            model.addAttribute("titulo", "Hecho");
            model.addAttribute("hecho", hecho);
            return "metamapa/mapa/hecho";
        }
        catch (Exception e) {
            ra.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se encontró el hecho.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/inicio";
        }
    }

    @GetMapping("/hechos/crear")
    public String crearHecho(Model model) {
        List<CategoriaDTO> categorias = this.metaMapaApiService.getCategorias(null);
        HechoInputDTO hecho = new HechoInputDTO();

        model.addAttribute("categorias", categorias);
        model.addAttribute("titulo", "Crear Hecho");
        model.addAttribute("hecho", hecho);
        return "metamapa/mapa/crear_hecho";
    }

    @PostMapping("/hechos/nuevo")
    public String nuevoHecho(@ModelAttribute HechoInputDTO hecho,
                             @RequestParam(value="mediaJson", required=false) String mediaJson,
                             RedirectAttributes redirectAttributes,
                             Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            hecho.setEstaRegistrado(true);
            hecho.setNombreUsuario(auth.getName());
        }
        else {
            hecho.setEstaRegistrado(false);
            hecho.setNombreUsuario(null);
        }
        try {
            List<MultimediaDTO> media = List.of();

            if (mediaJson != null && !mediaJson.isBlank()) {
                media = new ObjectMapper().readValue(mediaJson, new TypeReference<List<MultimediaDTO>>() {});
            }

            hecho.setMultimedia(media);

            this.dinamicaApiService.crearHecho(hecho);

            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "El hecho se a creado correctamente.",
                    "confirmButtonText", "Aceptar"
            ));

            return "redirect:/inicio";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudo crear el hecho." + e.getMessage() + e.getCause(),
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/hechos/crear";
        }
    }

    @GetMapping("/hechos/{id}/crear-solicitud")
    public String crearSolicitud(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            HechoDTO hecho = this.metaMapaApiService.getHecho(id);
            model.addAttribute("titulo", "Crear Solicitud");
            model.addAttribute("hecho", hecho);
            model.addAttribute("solicitud", new SolicitudInputDTO());
            return "metamapa/mapa/solicitud_eliminacion";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudo encontrar el hecho.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/inicio";
        }
    }

    @PostMapping("/hechos/{id}/solicitud-eliminacion")
    public String enviarSolicitud(@PathVariable Long id,
                                  @ModelAttribute SolicitudInputDTO solicitud,
                                  RedirectAttributes redirectAttributes,
                                  Authentication auth) {
        try {
            solicitud.setIdHechoAEliminar(id);
            if (auth != null && auth.isAuthenticated()) {
                solicitud.setNombreUsuario(auth.getName());
            }
            this.metaMapaApiService.crearSolicitud(solicitud);
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "Se ha creado la solicitud correctamente.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/hechos/" + id;
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudo crear la solicitud. " + e.getMessage() + e.getCause(),
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/hechos/" + id + "/crear-solicitud";
        }
    }
}
