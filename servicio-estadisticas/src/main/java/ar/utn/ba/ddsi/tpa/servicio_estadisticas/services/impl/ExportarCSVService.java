package ar.utn.ba.ddsi.tpa.servicio_estadisticas.services.impl;

import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.Categoria;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.Provincia;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.estadisticas.*;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.services.IEstadisticasService;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.services.IExportarCSVService;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExportarCSVService implements IExportarCSVService {
    private final IEstadisticasService estadisticasService;

    public ExportarCSVService(IEstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    public void exportarProvinciaConMasHechosEnUnaColeccion(PrintWriter writer, LocalDateTime desde, LocalDateTime hasta) {
        writer.println("Handle, Colección, Provincia, Cantidad de hechos");

        List<ProvinciaTopPorColeccion> resultados =
                estadisticasService.getProvinciaConMasHechosEnUnaColeccion(desde, hasta);
        for (ProvinciaTopPorColeccion resultado : resultados) {
            String handle = resultado.getColeccionHandle();
            if (handle == null) {
                handle = "null";
            }
            String nombreColeccion = resultado.getColeccionTitulo();
            if (nombreColeccion == null) {
                nombreColeccion = "null";
            }
            String provincia = resultado.getProvinciaNombre();
            if (provincia == null) {
                provincia = "null";
            }
            Long total = resultado.getTotalHechos();

            writer.printf("%s, %s, %s, %d", handle, nombreColeccion, provincia, total);
            writer.println();
        }
    }

    @Override
    public void exportarCategoriaConMasHechos(PrintWriter writer) {
        writer.println("Año, Mes, Categoria, Cantidad de hechos");
        List<CategoriaTopMensual> resultados = estadisticasService.getCategoriaConMasHechos();
        for (CategoriaTopMensual res : resultados) {
            Integer anio = res.getAnio();
            Integer mes = res.getMes();
            String categoria = res.getCategoriaNombre();
            Long total = res.getTotalHechos();
            writer.printf("%d, %d, %s, %d", anio, mes, categoria, total);
            writer.println();
        }
    }

    @Override
    public void exportarProvinciaConMasHechosEnUnaCategoria(PrintWriter writer, Long id_categoria) {
        writer.println("Año, Mes, Provincia, Cantidad de hechos");
        List<ProvinciaTopCategoriaMensual> resultados = estadisticasService.getProvinciaConMasHechosEnUnaCategoria(id_categoria);
        for (ProvinciaTopCategoriaMensual res : resultados) {
            Integer anio = res.getAnio();
            Integer mes = res.getMes();
            String provincia = res.getProvinciaNombre();
            Long total = res.getTotalHechos();
            writer.printf("%d, %d, %s, %d", anio, mes, provincia, total);
            writer.println();
        }
    }

    @Override
    public void exportarHoraConMasHechosEnUnaCategoria(PrintWriter writer, Long id_categoria) {
        writer.println("Año, Mes, Hora, Cantidad de hechos");
        List<HoraPicoMensualCategoria> resultados = estadisticasService.getHoraConMasHechosEnUnaCategoria(id_categoria);
        for (HoraPicoMensualCategoria res : resultados) {
            Integer anio = res.getAnio();
            Integer mes = res.getMes();
            Integer hora = res.getHora();
            Long total = res.getTotalHechos();
            writer.printf("%d, %d, %d, %d", anio, mes, hora, total);
            writer.println();
        }
    }

    @Override
    public void exportarCantidadDeSolicitudesSpam(PrintWriter writer) {
        writer.println("Año, Mes, Cantidad de solicitudes de spam");
        List<CantSpamMensual> resultados = estadisticasService.getCantidadDeSolicitudesSpam();
        for (CantSpamMensual res : resultados) {
            Integer anio = res.getAnio();
            Integer mes = res.getMes();
            Long cant = res.getTotalSpam();
            writer.printf("%d, %d, %d", anio, mes, cant);
            writer.println();
        }
    }

}
