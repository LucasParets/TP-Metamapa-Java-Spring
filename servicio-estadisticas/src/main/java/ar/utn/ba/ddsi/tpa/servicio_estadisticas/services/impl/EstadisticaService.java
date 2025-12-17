package ar.utn.ba.ddsi.tpa.servicio_estadisticas.services.impl;

import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.Categoria;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.Provincia;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.estadisticas.*;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.repositories.*;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.services.IEstadisticasService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EstadisticaService implements IEstadisticasService {
    private final IColeccionRepository coleccionRepository;
    private final IHechosRepository hechosRepository;
    private final IProvinciaRepository provinciaRepository;
    private final ICategoriasRepository categoriasRepository;
    private final ISolicitudesRepository solicitudesRepository;

    public EstadisticaService(IColeccionRepository coleccionRepository,
                            IHechosRepository hechosRepository,
                            IProvinciaRepository provinciaRepository,
                            ICategoriasRepository categoriasRepository,
                            ISolicitudesRepository solicitudesRepository) {
        this.coleccionRepository = coleccionRepository;
        this.hechosRepository = hechosRepository;
        this.provinciaRepository = provinciaRepository;
        this.categoriasRepository = categoriasRepository;
        this.solicitudesRepository = solicitudesRepository;
    }

    @Override
    public List<ProvinciaTopPorColeccion> getProvinciaConMasHechosEnUnaColeccion(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return coleccionRepository.provinciaTopPorColeccion(fechaInicio, fechaFin);
    }

    @Override
    public List<CategoriaTopMensual> getCategoriaConMasHechos() {
        return categoriasRepository.categoriaTopPorMesHistorico();
    }

    @Override
    public List<ProvinciaTopCategoriaMensual> getProvinciaConMasHechosEnUnaCategoria(Long id_categoria) {
        return categoriasRepository.provinciaTopPorCategoriaHistorico(id_categoria);
    }

    @Override
    public List<HoraPicoMensualCategoria> getHoraConMasHechosEnUnaCategoria(Long id_categoria) {
        return categoriasRepository.horaPicoMensualPorCategoria(id_categoria);
    }

    @Override
    public List<CantSpamMensual> getCantidadDeSolicitudesSpam() {
        return this.solicitudesRepository.spamPorMes();
    }
}
