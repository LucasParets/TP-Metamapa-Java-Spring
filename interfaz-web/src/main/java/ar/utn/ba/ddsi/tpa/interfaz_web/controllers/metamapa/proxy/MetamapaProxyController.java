package ar.utn.ba.ddsi.tpa.interfaz_web.controllers.metamapa.proxy;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.*;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input.SolicitudInputDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.metamapa.ColeccionMetamapaDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.metamapa.HechoMetamapaDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.MetaMapaApiService;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.metamapa.MetaMapaProxyService;
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
@RequestMapping("/metamapa")
public class MetamapaProxyController {
    private final MetaMapaProxyService mmService;
    private final MetaMapaApiService agregadorService;
    private final String apiGatewayUrl;

    public MetamapaProxyController(MetaMapaProxyService mmService,
                                   MetaMapaApiService agregadorService,
                                   @Value("${api-gateway.url}") String apiGatewayUrl) {
        this.mmService = mmService;
        this.apiGatewayUrl = apiGatewayUrl;
        this.agregadorService = agregadorService;
    }

    @GetMapping("/colecciones")
    public String colecciones(@PageableDefault Pageable pg,
                              @ModelAttribute FiltrosHechos f,
                              Model model) {
        try {
            PageResponse<ColeccionMetamapaDTO> coleccionesPage = mmService.getColecciones(pg);

            List<ColeccionMetamapaDTO> colecciones = coleccionesPage.getContent();

            model.addAttribute("colecciones", colecciones);
            model.addAttribute("filtros", f);
            model.addAttribute("titulo", "Colecciones Externas");
            model.addAttribute("cantHechos", coleccionesPage.getTotalElements());
            model.addAttribute("totalPages", coleccionesPage.getTotalPages());
            model.addAttribute("currentPage", coleccionesPage.getNumber());
            model.addAttribute("pageFirst", coleccionesPage.isFirst());
            model.addAttribute("pageLast", coleccionesPage.isLast());
            model.addAttribute("esMetamapa", true);
            return "metamapa/colecciones";
        } catch (Exception e) {
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

    @GetMapping("/colecciones/{handle}/hechos")
    public String getHechosDeColeccion(
            @PathVariable String handle,
            @ModelAttribute FiltrosHechos f,
            @PageableDefault(sort = "fechaHecho", direction= Sort.Direction.DESC) Pageable pg,
            @RequestParam String metamapa,
            Model model,
            RedirectAttributes ra) {

        try {
            ColeccionMetamapaDTO coleccion = this.mmService.getColeccion(handle, metamapa);

            List<CategoriaDTO> categorias = this.agregadorService.getCategorias(null);

            PageResponse<HechoMetamapaDTO> hechos = this.mmService.getHechos(handle, metamapa, f, pg);

            List<HechoMetamapaDTO> listaHechos = new ArrayList<>(hechos.getContent());

            model.addAttribute("coleccion", coleccion);
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
            model.addAttribute("esMetamapa", true);
            model.addAttribute("metamapa", metamapa);
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
            return "redirect:/metamapa/colecciones";
        }
    }

    @GetMapping("/hechos/{idHecho}")
    public String getHecho(
            @PathVariable Long idHecho,
            @RequestParam String metamapa,
            Model model,
            RedirectAttributes ra) {
        try {
            HechoMetamapaDTO hecho = this.mmService.getHecho(idHecho, metamapa);
            model.addAttribute("hechos", List.of(hecho));
            model.addAttribute("titulo", "Hecho");
            model.addAttribute("hecho", hecho);
            model.addAttribute("esMetamapa", true);
            model.addAttribute("metamapa", metamapa);
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

    @GetMapping("/hechos/{id}/crear-solicitud")
    public String crearSolicitud(@PathVariable Long id,
                                 @RequestParam String metamapa,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            HechoMetamapaDTO hecho = this.mmService.getHecho(id, metamapa);
            model.addAttribute("titulo", "Crear Solicitud");
            model.addAttribute("hecho", hecho);
            model.addAttribute("esMetamapa", true);
            model.addAttribute("metamapa", metamapa);
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
                                  @RequestBody String metamapa,
                                  RedirectAttributes redirectAttributes,
                                  Authentication auth) {
        try {
            solicitud.setIdHechoAEliminar(id);
            if (auth != null && auth.isAuthenticated()) {
                solicitud.setNombreUsuario(auth.getName());
            }
            this.mmService.crearSolicitud(solicitud, metamapa);
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "success",
                    "title", "¡Listo!",
                    "text", "Se ha creado la solicitud correctamente.",
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/metamapa/hechos/" + id + "?metamapa=" + metamapa;
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("swal", Map.of(
                    "icon", "error",
                    "title", "¡Error!",
                    "text", "No se pudo crear la solicitud. " + e.getMessage() + e.getCause(),
                    "confirmButtonText", "Aceptar"
            ));
            return "redirect:/metamapa/hechos/" + id + "/crear-solicitud?metamapa=" + metamapa;
        }
    }
}
