package ar.utn.ba.ddsi.tpa.fuente_estatica.services;

import ar.utn.ba.ddsi.tpa.fuente_estatica.models.dtos.input.EliminarHechoInputDTO;
import ar.utn.ba.ddsi.tpa.fuente_estatica.models.dtos.input.HechoInputDTO;
import ar.utn.ba.ddsi.tpa.fuente_estatica.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.hechos.Hecho;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface IHechosService {
    public List<HechoOutputDTO> mostrarHechos(String last_update);
    public void eliminarHecho(Long id);
    public HechoOutputDTO crearOutputDTO(Hecho hecho);
}
