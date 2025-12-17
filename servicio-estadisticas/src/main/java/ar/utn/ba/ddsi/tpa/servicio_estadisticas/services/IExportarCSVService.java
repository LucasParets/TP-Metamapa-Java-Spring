package ar.utn.ba.ddsi.tpa.servicio_estadisticas.services;

import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.time.LocalDateTime;

public interface IExportarCSVService {
    void exportarProvinciaConMasHechosEnUnaColeccion(PrintWriter writer, LocalDateTime desde, LocalDateTime hasta);
    void exportarCategoriaConMasHechos(PrintWriter writer);
    void exportarProvinciaConMasHechosEnUnaCategoria(PrintWriter writer, Long id_categoria);
    void exportarHoraConMasHechosEnUnaCategoria(PrintWriter writer, Long id_categoria);
    void exportarCantidadDeSolicitudesSpam(PrintWriter writer);
}