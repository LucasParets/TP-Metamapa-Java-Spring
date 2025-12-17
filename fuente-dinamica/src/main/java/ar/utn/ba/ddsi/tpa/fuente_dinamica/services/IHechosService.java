package ar.utn.ba.ddsi.tpa.fuente_dinamica.services;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.input.HechoInputDTO;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.input.RevisionHechoInputDTO;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.output.HechoOutputDTO;

import java.util.List;

public interface IHechosService {
    void cargarHecho(HechoInputDTO hechoInputDTO);
    void modificarHecho(Long id, HechoInputDTO hechoInputDTO);
    List<HechoOutputDTO> mostrarHechos(String last_update);
    HechoOutputDTO getHecho(Long id);
    void eliminarHecho(Long id);
}
