package ar.utn.ba.ddsi.tpa.servicio_agregacion.services;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.BboxMapaDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.FiltroHechosMetamapa;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.destacados.DestacadosInicioDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.destacados.HechoReducidoDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Categoria;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Etiqueta;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IHechosService {
    Page<HechoOutputDTO> mostrarHechosDeUnaColeccion(String handle, FiltroHechosMetamapa filtros, Pageable pg);
    Page<HechoOutputDTO> mostrarHechos(FiltroHechosMetamapa filtros, Pageable pg);
    Map<String, Object> mostrarHechosMapa(String handle, FiltroHechosMetamapa filtros, BboxMapaDTO bbox, PageRequest pageRequest);
    HechoOutputDTO hechoADTO(Hecho h);
    List<Categoria> mostrarCategorias(String handleColeccion);
    HechoOutputDTO mostrarHecho(Long id);
    void agregarEtiquetaAHecho(String etiquetaNombre, Long idHecho);
    void eliminarEtiquetaAHecho(String etiquetaNombre, Long idHecho);
    List<Etiqueta> mostrarEtiquetasDeHecho(Long idHecho);
    Page<Hecho> mostrarHechosDestacados(FiltroHechosMetamapa f, Pageable pg);
    DestacadosInicioDTO mostrarDestacadosInicio();
    void eliminarHecho(Long id, String admin);
}
