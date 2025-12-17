package ar.utn.ba.ddsi.tpa.interfaz_web.controllers.admin;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.CategoriaDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.MetaMapaApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/exportar-estadisticas")
public class ExportarEstadisticas {
    private final String urlServicioEstadisticas;
    private final MetaMapaApiService metaMapaApiService;

    public ExportarEstadisticas(@Value("${api-gateway-publico.url}") String apiGatewayUrl,
                                MetaMapaApiService metaMapaApiService) {
        this.urlServicioEstadisticas = apiGatewayUrl + "/estadistica";
        this.metaMapaApiService = metaMapaApiService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String exportarEstadisticas(Model model) {
        List<CategoriaDTO> categorias = metaMapaApiService.getCategorias(null);
        model.addAttribute("titulo", "Exportar Estadísticas");
        model.addAttribute("categorias", categorias);
        model.addAttribute("urlApi", urlServicioEstadisticas);
        return "admin/exportar_estadisticas";
    }
}
