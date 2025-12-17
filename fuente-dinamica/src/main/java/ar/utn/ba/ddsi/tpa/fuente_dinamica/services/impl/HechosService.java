package ar.utn.ba.ddsi.tpa.fuente_dinamica.services.impl;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.exceptions.hechos.HechoNoEncontradoException;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.exceptions.hechos.ModificacionHechoException;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.input.HechoInputDTO;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.Categoria;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.Multimedia;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.SolicitudModificacion;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.revision.EstadoRevision;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.revision.RevisionHecho;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.utilities.Coordenada;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.utilities.EstadoHecho;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.utilities.FechaParser;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.repositories.*;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.services.IHechosService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class HechosService implements IHechosService {
    private final IHechosRepository hechosRepository;
    private final ICategoriaRepository categoriaRepository;
    private final IMultimediaRepository multimediaRepository;
    private final IRevisionRepository revisionRepository;
    private final ISolicitudModificacionRepsoitory solicitudModificacionRepository;

    @Override
    public void cargarHecho(HechoInputDTO dto) {
        Hecho hecho = hechosRepository.save(this.crearHechoDeInputDTO(dto));
        revisionRepository.save(new RevisionHecho(hecho));
    }

    @Override
    public HechoOutputDTO getHecho(Long idHecho) {
        Hecho hecho = hechosRepository.findById(idHecho)
                .orElseThrow(() -> new HechoNoEncontradoException("No se encontró el hecho con el id " + idHecho));
        return crearOutputDTO(hecho);
    }

    @Override
    public List<HechoOutputDTO> mostrarHechos(String last_update) {
        if (last_update == null) {
            return hechosRepository.findAllByEstadoDelHecho(EstadoHecho.ACTIVO).stream().map(this::crearOutputDTO).toList();
        }
        return hechosRepository.findSinceLastUpdate(FechaParser.of(last_update), EstadoHecho.ACTIVO)
                .stream().map(this::crearOutputDTO).toList();
    }

    @Override
    public void modificarHecho(Long id, HechoInputDTO hechoInputDTO) {
        Hecho hechoAModificar = hechosRepository.findById(id)
                .orElseThrow(() -> new HechoNoEncontradoException("No se econtró el hecho con el id " + id));

        if (!hechoInputDTO.isEstaRegistrado()) {
            throw new ModificacionHechoException("El autor del hecho no esta registrado, por lo tanto no se puede modificar");
        }
        if (!hechoAModificar.estaDentroDelPlazoParaModificacion()) {
            throw new ModificacionHechoException("Se pasó el plazo para modificar el hecho");
        }
        if (!hechoAModificar.getNombreUsuario().equals(hechoInputDTO.getNombreUsuario())) {
            throw new ModificacionHechoException("El hecho no pertenece al usuario que intenta modificarlo");
        }

        RevisionHecho revision = revisionRepository.findByHechoId(hechoAModificar.getId());

        if (revision != null && revision.getEstado().equals(EstadoRevision.PENDIENTE)) {
            actualizarHecho(hechoAModificar, hechoInputDTO);
        }
        else {
            crearSolicitudDeModificacion(id, hechoInputDTO);
        }
    }

    private Hecho crearHechoDeInputDTO(HechoInputDTO dto) {
        Categoria categoria = new Categoria(dto.getCategoria());
        return Hecho
                .builder()
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .multimedia(dto.getMultimedia().stream().map(multimediaRepository::save
                ).toList())
                .categoria(categoriaRepository.findByNombre(categoria.getNombre())
                        .orElseGet(() -> categoriaRepository.save(categoria)))
                .nombreUsuario(dto.getNombreUsuario())
                .estaRegistrado(dto.isEstaRegistrado())
                .fechaHecho(dto.getFechaHecho())
                .fechaCarga(LocalDateTime.now())
                .fechaModificacion(LocalDateTime.now())
                .coordenada(new Coordenada(dto.getLatitud(), dto.getLongitud()))
                .estadoDelHecho(EstadoHecho.INACTIVO)
                .build();
    }

    private void crearSolicitudDeModificacion(Long id, HechoInputDTO dto) {
        Hecho hecho = hechosRepository.findById(id)
                .orElseThrow(() -> new HechoNoEncontradoException("No se econtró el hecho con el id " + id));

        SolicitudModificacion solicitud = new SolicitudModificacion();

        solicitud.setHecho(hecho);

        if (dto.getTitulo() != null && !dto.getTitulo().equals(hecho.getTitulo())) {
            solicitud.setTituloNuevo(dto.getTitulo());
        }
        if (dto.getDescripcion() != null && !dto.getDescripcion().equals(hecho.getDescripcion())) {
            solicitud.setDescripcionNueva(dto.getDescripcion());
        }
        if (dto.getCategoria() != null && !dto.getCategoria().equals(hecho.getCategoria().getNombre())) {
            Categoria categoria = categoriaRepository.findByNombre(dto.getCategoria())
                    .orElseGet(() -> categoriaRepository.save(new Categoria(dto.getCategoria())));
            solicitud.setCategoriaNueva(categoria);
        }
        if (dto.getLatitud() != null && !dto.getLatitud().equals(hecho.getCoordenada().getLatitud())) {
            solicitud.setLatitudNueva(dto.getLatitud());
        }
        if (dto.getLongitud() != null && !dto.getLongitud().equals(hecho.getCoordenada().getLongitud())) {
            solicitud.setLongitudNueva(dto.getLongitud());
        }
        if (dto.getFechaHecho() != null && !dto.getFechaHecho().equals(hecho.getFechaHecho())) {
            solicitud.setFechaHechoNueva(dto.getFechaHecho());
        }
        solicitudModificacionRepository.save(solicitud);
    }

    private void actualizarHecho(Hecho hecho, HechoInputDTO dto) {
        if (dto.getTitulo() != null) {
            hecho.setTitulo(dto.getTitulo());
        }
        if (dto.getDescripcion() != null) {
            hecho.setDescripcion(dto.getDescripcion());
        }
        if (dto.getCategoria() != null) {
            hecho.setCategoria(categoriaRepository.findByNombre(dto.getCategoria())
                    .orElseGet(() -> categoriaRepository.save(new Categoria(dto.getCategoria()))));
        }
        if (dto.getLatitud() != null) {
            hecho.getCoordenada().setLatitud(dto.getLatitud());
        }
        if (dto.getLongitud() != null) {
            hecho.getCoordenada().setLongitud(dto.getLongitud());
        }
        if (dto.getFechaHecho() != null) {
            hecho.setFechaHecho(dto.getFechaHecho());
        }
        hecho.setFechaModificacion(LocalDateTime.now());
        hechosRepository.save(hecho);
    }

    private HechoOutputDTO crearOutputDTO(Hecho hecho) {
        HechoOutputDTO dto = new HechoOutputDTO();

        dto.setId(hecho.getId());

        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria().getNombre());

        dto.setMultimedia(hecho.getMultimedia());

        dto.setLatitud(hecho.getCoordenada().getLatitud());
        dto.setLongitud(hecho.getCoordenada().getLongitud());
        dto.setFechaHecho(hecho.getFechaHecho());
        dto.setFechaCarga(hecho.getFechaCarga());
        dto.setFechaModificacion(hecho.getFechaModificacion());

        dto.setNombreUsuario(hecho.getNombreUsuario());
        dto.setEstaRegistrado(hecho.isEstaRegistrado());

        dto.setEstadoDelHecho(hecho.getEstadoDelHecho());

        return dto;
    }

    public void eliminarHecho(Long id) {
        hechosRepository.desactivarHecho(id, EstadoHecho.INACTIVO);
    }

}
