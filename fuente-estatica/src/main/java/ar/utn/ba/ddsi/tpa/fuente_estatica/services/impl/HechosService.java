package ar.utn.ba.ddsi.tpa.fuente_estatica.services.impl;

import ar.utn.ba.ddsi.tpa.fuente_estatica.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.utilities.EstadoHecho;
import ar.utn.ba.ddsi.tpa.fuente_estatica.models.entities.utilities.FechaParser;
import ar.utn.ba.ddsi.tpa.fuente_estatica.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.tpa.fuente_estatica.services.IHechosService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HechosService implements IHechosService {
    private final IHechosRepository hechosRepository;

    public HechosService(IHechosRepository hechosRepository) {
        this.hechosRepository = hechosRepository;
    }

    @Override
    public List<HechoOutputDTO> mostrarHechos(String last_update) {
        if (last_update == null) {
            return hechosRepository.findAll().stream().map(this::crearOutputDTO).toList();
        }
        return hechosRepository.findSinceLastUpdate(FechaParser.of(last_update)).stream().map(this::crearOutputDTO).toList();
    }

   @Override
    public void eliminarHecho(Long id) {
        Hecho hecho = hechosRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontro el hecho"));
        hecho.setEstadoDelHecho(EstadoHecho.INACTIVO);
        hechosRepository.save(hecho);
    }

    @Override
    public HechoOutputDTO crearOutputDTO(Hecho hecho) {
        HechoOutputDTO dto = new HechoOutputDTO();

        dto.setId(hecho.getId());

        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());

        dto.setCategoria(hecho.getCategoria().getNombre());

        dto.setLatitud(hecho.getCoordenada().getLatitud());

        dto.setLongitud(hecho.getCoordenada().getLongitud());

        dto.setFechaHecho(hecho.getFechaHecho());
        dto.setFechaCarga(hecho.getFechaCarga());
        dto.setFechaModificacion(hecho.getFechaModificacion());

        dto.setEstadoDelHecho(hecho.getEstadoDelHecho());

        dto.setNombreArchivo(hecho.getFuente().getNombreDataset());

        return dto;
    }
}
