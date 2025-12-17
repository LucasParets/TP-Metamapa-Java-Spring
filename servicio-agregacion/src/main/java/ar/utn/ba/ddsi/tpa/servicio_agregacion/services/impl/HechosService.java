package ar.utn.ba.ddsi.tpa.servicio_agregacion.services.impl;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.BboxMapaDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.destacados.ColeccionReducidaDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.destacados.DestacadosInicioDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.destacados.HechoReducidoDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.condiciones.CondicionContieneEtiqueta;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.EstadoHecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.EstadoSolicitud;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.*;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.IFuentesService;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.utils.HechoSpecs;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.exceptions.hecho.HechoNoEncontradoException;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.FiltroHechosMetamapa;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.*;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenDinamica;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenEstatica;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenProxy;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.FechaParser;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.IHechosService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class HechosService implements IHechosService {
    private final IHechosRepository hechosRepository;
    private final ICategoriasRepository categoriasRepository;
    private final IEtiquetasRepository etiquetasRepository;
    private final IColeccionRepository coleccionRepository;
    private final ISolicitudEliminacionRepository solicitudRepository;
    private final IFuentesService fuentesService;

    public Page<HechoOutputDTO> mostrarHechos(FiltroHechosMetamapa filtros, Pageable pg) {
        Specification<Hecho> specs = HechoSpecs.filtros(filtros);

        Page<Hecho> hechos = hechosRepository.findAll(specs, pg);

        return hechos.map(this::hechoADTO);
    }

    public Page<HechoOutputDTO> mostrarHechosDeUnaColeccion(String handle, FiltroHechosMetamapa filtros, Pageable pg) {
        Specification<Hecho> specs;

        if (filtros.getModo_navegacion() != null && filtros.getModo_navegacion().equals("CURADA"))
            specs = Specification.where(HechoSpecs.filtros(filtros))
                    .and(HechoSpecs.enColeccionConsensuados(handle));
        else
            specs = Specification.where(HechoSpecs.filtros(filtros))
                .and(HechoSpecs.enColeccion(handle));

        Page<Hecho> hechos = hechosRepository.findAll(specs, pg);

        return hechos.map(this::hechoADTO);
    }

    public Map<String, Object> mostrarHechosMapa(String handle, FiltroHechosMetamapa filtros, BboxMapaDTO bbox, PageRequest pr) {
        Specification<Hecho> specs;

        if (handle != null)
            specs = HechoSpecs.filtros(filtros).and(HechoSpecs.bbox(bbox))
                .and(HechoSpecs.enColeccion(handle));
        else
            specs = HechoSpecs.filtros(filtros).and(HechoSpecs.bbox(bbox));

        Page<Hecho> hechos = hechosRepository.findAll(specs, pr);

        var feats = hechos.map(h -> Map.of(
                "type","Feature",
                "geometry", Map.of(
                        "type","Point",
                        "coordinates", List.of(h.getCoordenada().getLongitud(), h.getCoordenada().getLatitud()),
                "properties", Map.of(
                            "id", h.getId(),
                            "titulo", h.getTitulo(),
                            "fecha", FechaParser.toString(h.getFechaHecho()),
                            "categoria", h.getCategoria().getNombre())
        )));

        return Map.of("type", "FeatureCollection", "features", feats.toList());
    }

    public HechoOutputDTO mostrarHecho(Long id) {
        Hecho h = hechosRepository.findById(id)
                .orElseThrow(() -> new HechoNoEncontradoException("No se ha encontrado el hecho con id: " + id));
        return hechoADTO(h);
    }

    public HechoOutputDTO hechoADTO(Hecho h) {
        HechoOutputDTO dto = new HechoOutputDTO();
        dto.setId(h.getId());
        dto.setTitulo(h.getTitulo());
        dto.setCategoria(h.getCategoria().getNombre());
        dto.setMultimedia(h.getMultimedia());
        dto.setEtiquetas(h.getEtiquetas().stream().map(Etiqueta::getNombre).toList());
        dto.setDescripcion(h.getDescripcion());
        dto.setLatitud(h.getCoordenada().getLatitud());
        dto.setLongitud(h.getCoordenada().getLongitud());
        dto.setProvincia(h.getProvincia() != null ? h.getProvincia().getNombre() : null);
        dto.setDepartamento(h.getDepartamento() != null ? h.getDepartamento().getNombre() : null);
        dto.setFechaHecho(h.getFechaHecho());
        dto.setFechaCarga(h.getFechaCarga());
        dto.setFechaModificacion(h.getFechaModificacion());
        dto.setEstadoDelHecho(h.getEstadoDelHecho());
        dto.setFuente(h.getOrigen().getFuente().toString());
        dto.setOrigenHecho(h.getOrigen().getNombreOrigen());
        return dto;
    }

    public List<Categoria> mostrarCategorias(String handleColeccion) {
        if (handleColeccion == null) {
            Sort sort = Sort.by(Sort.Direction.ASC, "nombre");
            return categoriasRepository.findAll(sort);
        }
        else {
            return categoriasRepository.findDistinctByColeccionHandle(handleColeccion);
        }
    }

    public void agregarEtiquetaAHecho(String etiquetaNombre, Long idHecho) {
        Hecho hecho = hechosRepository.findById(idHecho)
                .orElseThrow(() -> new HechoNoEncontradoException("No se ha encontrado el hecho con id: " + idHecho));

        Etiqueta etiqueta = etiquetasRepository.findByNombre(etiquetaNombre)
                .orElseGet(() -> etiquetasRepository.save(new Etiqueta(etiquetaNombre)));

        hecho.agregarEtiqueta(etiqueta);

        hechosRepository.save(hecho);
    }

    public void eliminarEtiquetaAHecho(String etiquetaNombre, Long idHecho) {
        Hecho hecho = hechosRepository.findById(idHecho)
                .orElseThrow(() -> new HechoNoEncontradoException("No se ha encontrado el hecho con id: " + idHecho));

        Etiqueta etiqueta = etiquetasRepository.findByNombre(etiquetaNombre)
                .orElseThrow(() -> new RuntimeException("No se encontró la etiqueta"));

        hecho.eliminarEtiqueta(etiqueta);

        hechosRepository.save(hecho);
    }

    public List<Etiqueta> mostrarEtiquetasDeHecho(Long idHecho) {
        return etiquetasRepository.findAllByHecho(idHecho);
    }

    public Page<Hecho> mostrarHechosDestacados(FiltroHechosMetamapa f, Pageable pg) {
        CondicionContieneEtiqueta condicion = new CondicionContieneEtiqueta("destacado");
        Specification<Hecho> specs = condicion.cumple();

        specs.and(HechoSpecs.filtros(f));

        return hechosRepository.findAll(specs, pg);
    }

    private List<ColeccionReducidaDTO> mostrarColeccionesDestacadas() {
        PageRequest pr = PageRequest.of(0, 10);
        Specification<Coleccion> specs = ((root, cq, cb) ->
                cb.equal(root.get("esDestacado"), true));
        return coleccionRepository.findAll(specs, pr).map(c -> {
            ColeccionReducidaDTO dto = new ColeccionReducidaDTO();
            dto.setHandle(c.getHandle());
            dto.setTitulo(c.getTitulo());
            dto.setDescripcion(c.getDescripcion());
            return dto;
        }).getContent();
    }

    public DestacadosInicioDTO mostrarDestacadosInicio() {
        DestacadosInicioDTO dto = new DestacadosInicioDTO();
        FiltroHechosMetamapa f = new FiltroHechosMetamapa();

        PageRequest pr = PageRequest
                .of(0, 10, Sort.by(Sort.Direction.DESC, "fechaCarga"));
        dto.setHechosDestacados(this.mostrarHechosDestacados(f, pr)
                .map(this::hechoADTOReducido)
                .getContent());

        dto.setColeccionesDestacadas(this.mostrarColeccionesDestacadas());

        return dto;
    }

    private HechoReducidoDTO hechoADTOReducido(Hecho h) {
        HechoReducidoDTO dto = new HechoReducidoDTO();
        dto.setId(h.getId());
        dto.setTitulo(h.getTitulo());
        dto.setDescripcion(h.getDescripcion());
        return dto;
    }

    @Transactional
    public void eliminarHecho(Long id, String admin) {
        Hecho hecho = hechosRepository.findById(id)
                .orElseThrow(() -> new HechoNoEncontradoException("No se ha encontrado el hecho con id: " + id));

        hecho.setEstadoDelHecho(EstadoHecho.INACTIVO);

        solicitudRepository.updateEstadoByHechoId(id, EstadoSolicitud.ACEPTADA, LocalDateTime.now(), admin);

        WebClient webClient = fuentesService.getFuenteWebClient(hecho.getOrigen().getFuente());
        Long idHecho = hecho.getIdCargadoEnOrigen();
        webClient.delete()
                .uri(ub -> ub.path("/hechos/" + idHecho).build())
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
