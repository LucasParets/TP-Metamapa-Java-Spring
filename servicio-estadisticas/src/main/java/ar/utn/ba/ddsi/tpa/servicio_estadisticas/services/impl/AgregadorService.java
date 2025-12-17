package ar.utn.ba.ddsi.tpa.servicio_estadisticas.services.impl;

import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.dtos.ColeccionInputDTO;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.dtos.HechoInputDTO;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.dtos.SolicitudInputDTO;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.entities.*;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.models.repositories.*;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.services.IAgregadorService;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.utils.FechaParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AgregadorService implements IAgregadorService {
    private final String url;
    private final WebClient webClient;
    private final IColeccionRepository coleccionRepository;
    private final IHechosRepository hechosRepository;
    private final IProvinciaRepository provinciaRepository;
    private final ICategoriasRepository categoriasRepository;
    private final ISolicitudesRepository solicitudesRepository;

    public AgregadorService(@Value("${servicio_agregador.api.url}") String url,
                            WebClient.Builder webClientBuilder,
                            IColeccionRepository coleccionRepository,
                            IHechosRepository hechosRepository,
                            IProvinciaRepository provinciaRepository,
                            ICategoriasRepository categoriasRepository,
                            ISolicitudesRepository solicitudesRepository) {
        this.url = url;
        this.webClient = webClientBuilder.baseUrl(url).build();
        this.coleccionRepository = coleccionRepository;
        this.hechosRepository = hechosRepository;
        this.provinciaRepository = provinciaRepository;
        this.categoriasRepository = categoriasRepository;
        this.solicitudesRepository = solicitudesRepository;
    }

    private List<ColeccionInputDTO> buscarColecciones() {
        return webClient.get()
                .uri(ub -> ub
                        .path("/estadisticas/colecciones")
                        .build()
                )
                .retrieve()
                .bodyToFlux(ColeccionInputDTO.class)
                .collectList()
                .block();
    }

    private List<HechoInputDTO> buscarHechos(LocalDateTime lastUpdate) {
        return webClient.get()
                .uri(ub -> ub
                        .path("/estadisticas/hechos")
                        .queryParam("lastUpdate", FechaParser.toString(lastUpdate))
                        .build()
                )
                .retrieve()
                .bodyToFlux(HechoInputDTO.class)
                .collectList()
                .block();
    }

    // Traer solo las resueltas
    private List<SolicitudInputDTO> buscarSolicitudes(LocalDateTime lastUpdate) {
        return webClient.get()
                .uri(ub -> ub
                        .path("/estadisticas/solicitudes")
                        .queryParam("lastUpdate", FechaParser.toString(lastUpdate))
                        .build()
                )
                .retrieve()
                .bodyToFlux(SolicitudInputDTO.class)
                .collectList()
                .block();
    }

    @Transactional
    public void traerHechosDelAgregador() {
        LocalDateTime lastUpdate = hechosRepository.findMaxFechaModificacion();
        if (lastUpdate == null) lastUpdate = LocalDateTime.of(1970,1,1,0,0);
        List<HechoInputDTO> hechosDto = this.buscarHechos(lastUpdate);
        for (HechoInputDTO dto : hechosDto) {
            if (dto.getEstado() == EstadoHecho.INACTIVO) {
                try {
                    hechosRepository.deleteById(dto.getId());
                }
                catch (Exception e) {
                    continue;
                }
            }
            else {
                Hecho hecho = crearHechoDeUnDTO(dto);
                hechosRepository.save(hecho);
            }
        }
    }

    @Transactional
    public void traerColeccionesDelAgregador() {
        List<ColeccionInputDTO> coleccionesDto = this.buscarColecciones();
        for (ColeccionInputDTO dto : coleccionesDto) {
            Optional<Coleccion> coleccionOp = coleccionRepository.findByHandle(dto.getHandle());
            if (coleccionOp.isEmpty()) {
                Coleccion coleccion = crearColeccionDeUnDTO(dto);
                List<Hecho> hechos = hechosRepository.findAllById(dto.getHechos());
                coleccion.setHechos(hechos);
                coleccionRepository.save(coleccion);
            }
            else
                coleccionOp.get().setHechos(hechosRepository.findAllById(dto.getHechos()));
        }
    }

    @Transactional
    public void traerSolicitudesDelAgregador() {
        LocalDateTime lastUpdate = solicitudesRepository.findMaxFechaResolucion();
        if (lastUpdate == null) lastUpdate = LocalDateTime.of(1970,1,1,0,0);
        List<SolicitudInputDTO> solicitudesDto = this.buscarSolicitudes(lastUpdate);
        for (SolicitudInputDTO dto : solicitudesDto) {
            SolicitudEliminacion s = crearSolicitudDeUnDTO(dto);
            solicitudesRepository.save(s);
        }
    }

    private Coleccion crearColeccionDeUnDTO(ColeccionInputDTO dto) {
        Coleccion coleccion = new Coleccion();
        coleccion.setHandle(dto.getHandle());
        coleccion.setTitulo(dto.getTitulo());
        return coleccion;
    }

    private Hecho crearHechoDeUnDTO(HechoInputDTO dto) {
        Hecho hecho = new Hecho();
        hecho.setId(dto.getId());
        hecho.setTitulo(dto.getTitulo());
        hecho.setFechaHecho(dto.getFechaHecho());
        hecho.setFechaModificacion(dto.getFechaModificacion());

        Categoria categoria = categoriasRepository.findByNombreIgnoreCase(dto.getCategoria_nombre())
                .orElseGet(() -> categoriasRepository.save(new Categoria(dto.getCategoria_id(), dto.getCategoria_nombre())));
        hecho.setCategoria(categoria);

        Provincia provincia;
        if (dto.getId_provincia() == null) {
            provincia = null;
        }
        else provincia = provinciaRepository.findById(dto.getId_provincia())
                .orElse(null);
        hecho.setProvincia(provincia);

        return hecho;
    }

    private SolicitudEliminacion crearSolicitudDeUnDTO(SolicitudInputDTO dto) {
        SolicitudEliminacion s = new SolicitudEliminacion();
        s.setId(dto.getId());
        s.setEsSpam(dto.getEsSpam());
        s.setFechaResolucion(dto.getFechaResolucion());
        return s;
    }
}
