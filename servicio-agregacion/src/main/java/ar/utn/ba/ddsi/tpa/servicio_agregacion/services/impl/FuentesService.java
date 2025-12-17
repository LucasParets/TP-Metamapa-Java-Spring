package ar.utn.ba.ddsi.tpa.servicio_agregacion.services.impl;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.HechoFuenteDinamicaDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.HechoFuenteEstaticaDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.HechoFuenteProxyDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.fuentes.TipoFuente;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Categoria;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Multimedia;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.Coordenada;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenDinamica;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenEstatica;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenProxy;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.EstadoHecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.FechaParser;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.*;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.IFuentesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class FuentesService implements IFuentesService {
    private final IHechosRepository hechosRepository;
    private final IOrigenRepository origenRepository;
    private final ICategoriasRepository categoriaRepository;
    private final IMultimediaRepository multimediaRepository;
    private final WebClient webClientEstatica;
    private final WebClient webClientDinamica;
    private final WebClient webClientProxy;
    private final String estaticaUrl;
    private final String proxyUrl;
    private final String dinamicaUrl;
    private final SyncService syncService;

    public FuentesService(IHechosRepository hechosRepository,
                          @Value("${fuente_estatica.api.url}") String urlFuenteEstatica,
                          @Value("${fuente_dinamica.api.url}") String urlFuenteDinamica,
                          @Value("${fuente_proxy.api.url}") String urlFuenteProxy,
                          WebClient.Builder webClientBuilder,
                          IOrigenRepository origenRepository,
                          ICategoriasRepository categoriaRepository,
                          IMultimediaRepository multimediaRepository,
                          SyncService syncService) {
        this.hechosRepository = hechosRepository;
        this.webClientEstatica =webClientBuilder.baseUrl(urlFuenteEstatica).build();
        this.webClientDinamica = webClientBuilder.baseUrl(urlFuenteDinamica).build();
        this.webClientProxy = webClientBuilder.baseUrl(urlFuenteProxy).build();
        this.estaticaUrl = urlFuenteEstatica;
        this.proxyUrl = urlFuenteProxy;
        this.dinamicaUrl = urlFuenteDinamica;
        this.syncService = syncService;
        this.origenRepository = origenRepository;
        this.categoriaRepository = categoriaRepository;
        this.multimediaRepository = multimediaRepository;
    }

    private <T> List<T> obtenerHechos(WebClient webClient, LocalDateTime since, Class<T> dtoClass) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/hechos")
                        .queryParam("last_update", FechaParser.toString(since))
                        .build())
                .retrieve()
                .bodyToFlux(dtoClass)
                .collectList()
                .block();
    }

    public <T> boolean insertarHechos(List<T> dtoList, TipoFuente tipoFuente) {
        AtomicReference<LocalDateTime> maxUpdated = new AtomicReference<>();

        boolean huboCambios = false;

        for (T dto : dtoList) {
            Hecho h = this.DTOaHecho(dto, tipoFuente);
            Optional<Hecho> optionalHecho = hechosRepository
                    .findByIdCargadoEnOrigenAndOrigen(h.getIdCargadoEnOrigen(), h.getOrigen());

            if(optionalHecho.isEmpty()) {
                hechosRepository.save(h);
                huboCambios = true;
            }
            else {
                Hecho hechoEnBBD = optionalHecho.get();
                if(hechoEnBBD.getEstadoDelHecho().equals(EstadoHecho.ACTIVO)
                        && h.getFechaModificacion().isAfter(hechoEnBBD.getFechaModificacion())) {
                    h.setId(hechoEnBBD.getId());
                    h.setEtiquetas(hechoEnBBD.getEtiquetas());
                    h.setProvincia(hechoEnBBD.getProvincia());
                    h.setDepartamento(hechoEnBBD.getDepartamento());
                    hechosRepository.save(h);
                    huboCambios = true;
                }
            }

            LocalDateTime fechaMod = h.getFechaModificacion();
            maxUpdated.updateAndGet(current ->
                    current == null || fechaMod.isAfter(current) ? fechaMod : current
            );
        }

        if (maxUpdated.get() != null || dtoList.isEmpty()) {
            this.syncService.bump(tipoFuente, maxUpdated.get());
        }

        return huboCambios;
    }

    public boolean traerHechosDeFuenteEstatica() {
        LocalDateTime since = syncService.getSince(TipoFuente.ESTATICA);
        List<HechoFuenteEstaticaDTO> hechos = obtenerHechos(webClientEstatica, since, HechoFuenteEstaticaDTO.class);
        return insertarHechos(hechos, TipoFuente.ESTATICA);
    }

    public boolean traerHechosDeFuenteDinamica() {
        LocalDateTime since = syncService.getSince(TipoFuente.DINAMICA);
        List<HechoFuenteDinamicaDTO> hechos = obtenerHechos(webClientDinamica, since, HechoFuenteDinamicaDTO.class);
        return insertarHechos(hechos, TipoFuente.DINAMICA);
    }

    public boolean traerHechosDeFuenteProxy() {
        LocalDateTime since = syncService.getSince(TipoFuente.PROXY);
        List<HechoFuenteProxyDTO> hechos = obtenerHechos(webClientProxy, since, HechoFuenteProxyDTO.class);
        return insertarHechos(hechos, TipoFuente.PROXY);
    }

    private <T> Hecho DTOaHecho(T dto, TipoFuente tipoFuente) {
        if (tipoFuente == TipoFuente.ESTATICA) {
            return this.crearHechoDeUnDTO((HechoFuenteEstaticaDTO) dto);
        }
        else if (tipoFuente == TipoFuente.DINAMICA) {
            return this.crearHechoDeUnDTO((HechoFuenteDinamicaDTO) dto);
        }
        else {
            return this.crearHechoDeUnDTO((HechoFuenteProxyDTO) dto);
        }
    }

    private Hecho crearHechoDeUnDTO(HechoFuenteEstaticaDTO dto) {
        Categoria categoria = categoriaRepository.findByNombre(dto.getCategoria())
                .orElseGet(() -> categoriaRepository.save(new Categoria(dto.getCategoria())));
        OrigenEstatica origen = origenRepository.findByNombreArchivo(dto.getNombreArchivo())
                .orElseGet(() -> origenRepository.save(new OrigenEstatica(dto.getNombreArchivo())));
        return Hecho.builder()
                .titulo(dto.getTitulo())
                .idCargadoEnOrigen(dto.getId())
                .descripcion(dto.getDescripcion())
                .categoria(categoria)
                .coordenada(new Coordenada(dto.getLatitud(), dto.getLongitud()))
                .fechaHecho(dto.getFechaHecho())
                .fechaCarga(dto.getFechaCarga())
                .fechaModificacion(dto.getFechaModificacion())
                .estadoDelHecho(dto.getEstadoDelHecho())
                .geocodificado(false)
                .origen(origen)
                .build();
    }

    private Hecho crearHechoDeUnDTO(HechoFuenteDinamicaDTO dto) {
        Categoria categoria = categoriaRepository.findByNombre(dto.getCategoria())
                .orElseGet(() -> categoriaRepository.save(new Categoria(dto.getCategoria())));

        OrigenDinamica origen;
        if (!dto.isEstaRegistrado() || dto.getNombreUsuario() == null || dto.getNombreUsuario().isBlank()) {
            origen = new OrigenDinamica("Anónimo");
            origenRepository.save(origen);
        }
        else {
            origen = origenRepository.findByNombreUsuario(dto.getNombreUsuario())
                    .orElseGet(() -> origenRepository.save(new OrigenDinamica(dto.getNombreUsuario())));
        }

        List<Multimedia> listaMm = new ArrayList<>();
        for (Multimedia mm : dto.getMultimedia()) {
            Multimedia multimedia = multimediaRepository.findByPublicId(mm.getPublicId())
                    .orElseGet(() -> multimediaRepository.save(
                            new Multimedia(mm.getUrl(), mm.getTipo(), mm.getPublicId())));
            listaMm.add(multimedia);
        }

        return Hecho.builder()
                .idCargadoEnOrigen(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .categoria(categoria)
                .multimedia(listaMm)
                .coordenada(new Coordenada(dto.getLatitud(), dto.getLongitud()))
                .fechaHecho(dto.getFechaHecho())
                .fechaCarga(dto.getFechaCarga())
                .fechaModificacion(dto.getFechaModificacion())
                .estadoDelHecho(dto.getEstadoDelHecho())
                .geocodificado(false)
                .origen(origen)
                .build();
    }

    private Hecho crearHechoDeUnDTO(HechoFuenteProxyDTO dto) {
        Categoria categoria = categoriaRepository.findByNombre(dto.getCategoria())
                .orElseGet(() -> categoriaRepository.save(new Categoria(dto.getCategoria())));
        OrigenProxy origen = origenRepository.findByNombreApi(dto.getOrigenHecho())
                .orElseGet(() -> origenRepository.save(new OrigenProxy(dto.getOrigenHecho())));
        return Hecho.builder()
                .idCargadoEnOrigen(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .categoria(categoria)
                .coordenada(new Coordenada(dto.getLatitud(), dto.getLongitud()))
                .fechaHecho(dto.getFechaHecho())
                .fechaCarga(dto.getFechaCarga())
                .fechaModificacion(dto.getFechaModificacion())
                .estadoDelHecho(dto.getEstadoDelHecho())
                .geocodificado(false)
                .origen(origen)
                .build();
    }

    public String getUrl(TipoFuente fuente) {
        if (fuente == TipoFuente.ESTATICA) {
            return this.estaticaUrl;
        }
        else if (fuente == TipoFuente.DINAMICA) {
            return this.dinamicaUrl;
        }
        else {
            return this.proxyUrl;
        }
    }

    public WebClient getFuenteWebClient(TipoFuente fuente) {
        if (fuente == TipoFuente.ESTATICA) {
            return this.webClientEstatica;
        }
        else if (fuente == TipoFuente.DINAMICA) {
            return this.webClientDinamica;
        }
        else {
            return this.webClientProxy;
        }
    }

    public int getCantFuentes() {
        return (int) origenRepository.count();
    }
}
