package ar.utn.ba.ddsi.tpa.servicio_agregacion.services;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.CriterioInputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.FiltroColecciones;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.CriterioOutputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.fuentes.TipoFuente;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Coleccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IColeccionService {
    Page<ColeccionOutputDTO> mostrarColecciones(FiltroColecciones f, Pageable pg);
    ColeccionOutputDTO mostrarColeccion(String handle);
    String crearColeccion(ColeccionInputDTO dto);
    void eliminarColeccion(String handle);
    String modificarColeccion(String handle, ColeccionInputDTO dto);
    List<HechoOutputDTO> mostrarHechos(String handle);
    String agregarCriterio(String handle, CriterioInputDTO dto);
    String quitarCriterio(String handle, Long idCriterio);
    //void refrescarColecciones();
    List<CriterioOutputDTO> mostrarCriterios(String handle);
    void refrescarColeccion(String handle);
    void destacarColeccion(String handle);
    void quitarDestacado(String handle);
    void refrescarColeccionesPorFuente(TipoFuente fuente);
}
