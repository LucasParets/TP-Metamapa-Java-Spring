package ar.utn.ba.ddsi.tpa.servicio_estadisticas.controllers;

import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.Categoria;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.Provincia;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.estadisticas.*;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.services.IAgregadorService;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.services.IEstadisticasService;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.services.IExportarCSVService;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.services.impl.ExportarCSVService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/estadistica")
public class EstadisticasController {
    private final IAgregadorService agregadorService;
    private final IEstadisticasService estadisticasService;
    private final IExportarCSVService exportarCSVService;

    public EstadisticasController(IAgregadorService agregadorService,
                                  IEstadisticasService estadisticasService,
                                  IExportarCSVService exportarCSVService) {
        this.agregadorService = agregadorService;
        this.estadisticasService = estadisticasService;
        this.exportarCSVService = exportarCSVService;
    }

    @GetMapping(value = "/provincias_top")
    public List<ProvinciaTopPorColeccion> provinciaConMasHechosEnUnaColeccion(@RequestParam LocalDateTime desde,
                                                                              @RequestParam LocalDateTime hasta) {
        return this.estadisticasService.getProvinciaConMasHechosEnUnaColeccion(desde, hasta);
    }

    @GetMapping(value = "/categoria_top")
    public List<CategoriaTopMensual> categoriaConMasHechos() {
        return this.estadisticasService.getCategoriaConMasHechos();
    }

    @GetMapping(value = "/provincia_top_x_categoria/{id_categoria}")
    public List<ProvinciaTopCategoriaMensual> provinciaConMasHechosEnUnaCategoria(@PathVariable Long id_categoria) {
        return this.estadisticasService.getProvinciaConMasHechosEnUnaCategoria(id_categoria);
    }

    @GetMapping(value = "/horario_top_x_categoria/{id_categoria}")
    public List<HoraPicoMensualCategoria> getHoraConMasHechosEnUnaCategoria(@PathVariable Long id_categoria) {
        return this.estadisticasService.getHoraConMasHechosEnUnaCategoria(id_categoria);
    }

    @GetMapping(value = "/cant_spam")
    public List<CantSpamMensual> getCantidadDeSolicitudesSpam() {
        return this.estadisticasService.getCantidadDeSolicitudesSpam();
    }

    @GetMapping(value = "/provincias_top/csv")
    public void exportarProvinciaConMasHechosEnUnaColeccion(@RequestParam LocalDateTime desde,
                                                            @RequestParam LocalDateTime hasta,
                                                            HttpServletResponse response)  throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"provincias_top.csv\"");

        try (PrintWriter writer = response.getWriter() ) {
            this.exportarCSVService.exportarProvinciaConMasHechosEnUnaColeccion(writer, desde, hasta);
        }
    }

    @GetMapping(value = "/categoria_top/csv")
    public void exportarCategoriaConMasHechos(HttpServletResponse response)  throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"categorias_top.csv\"");

        try (PrintWriter writer = response.getWriter() ) {
            this.exportarCSVService.exportarCategoriaConMasHechos(writer);
        }
    }

    @GetMapping(value = "/provincia_top_x_categoria/{id_categoria}/csv")
    public void exportarProvinciaConMasHechosEnUnaCategoria(@PathVariable Long id_categoria, HttpServletResponse response)  throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"provincias_top_x_categoria.csv\"");

        try (PrintWriter writer = response.getWriter() ) {
            this.exportarCSVService.exportarProvinciaConMasHechosEnUnaCategoria(writer, id_categoria);
        }
    }

    @GetMapping(value = "/horario_top_x_categoria/{id_categoria}/csv")
    public void exportarHorarioConMasHechosEnUnaCategoria(@PathVariable Long id_categoria, HttpServletResponse response)  throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"horario_top_x_categoria.csv\"");

        try (PrintWriter writer = response.getWriter() ) {
            this.exportarCSVService.exportarHoraConMasHechosEnUnaCategoria(writer, id_categoria);
        }
    }

    @GetMapping(value = "/cant_spam/csv")
    public void exportarCantidadDeSolicitudesSpam(HttpServletResponse response)  throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"cant_spam.csv\"");

        try (PrintWriter writer = response.getWriter() ) {
            this.exportarCSVService.exportarCantidadDeSolicitudesSpam(writer);
        }
    }
}
