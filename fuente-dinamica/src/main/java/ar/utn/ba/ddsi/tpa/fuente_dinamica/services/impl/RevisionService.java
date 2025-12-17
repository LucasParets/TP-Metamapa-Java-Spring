package ar.utn.ba.ddsi.tpa.fuente_dinamica.services.impl;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.input.RevisionHechoInputDTO;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.output.RevisionOutputDTO;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.revision.EstadoRevision;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.revision.RevisionHecho;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.repositories.IRevisionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RevisionService {
    private final IRevisionRepository revisionRepository;
    private final IHechosRepository hechosRepository;

    public RevisionService(IRevisionRepository revisionRepository,
                           IHechosRepository hechosRepository) {
        this.revisionRepository = revisionRepository;
        this.hechosRepository = hechosRepository;
    }

    public Page<RevisionOutputDTO> mostrarRevisiones(Pageable pg, EstadoRevision estado) {
        Specification<RevisionHecho> spec = null;

        if (estado != null) {
            spec = estadoRevision(estado);
        }

        Page<RevisionHecho> revisiones = spec == null ?
                this.revisionRepository.findAll(pg) :
                this.revisionRepository.findAll(spec, pg);

        return revisiones.map(this::revisionADTO);
    }

    public RevisionOutputDTO revisionADTO(RevisionHecho r) {
        RevisionOutputDTO dto = new RevisionOutputDTO();
        Hecho h = r.getHecho();
        dto.setIdRevision(r.getId());
        dto.setEstadoRevision(r.getEstado());
        dto.setSugerencia(r.getSugerencia());
        dto.setIdHecho(h.getId());
        dto.setTituloHecho(h.getTitulo());
        dto.setNombreUsuario(h.getNombreUsuario());
        dto.setFechaCreacionHecho(h.getFechaCarga());
        dto.setFechaRevision(r.getFechaRevision());
        dto.setNombreAdmin(r.getUsuarioAdmin());
        return dto;
    }

    @Transactional
    public void revisarHecho(Long idRevision, RevisionHechoInputDTO dto) {
        RevisionHecho revision = revisionRepository.findById(idRevision)
                .orElseThrow(() -> new IllegalArgumentException("No se encontro la revision con el id " + idRevision));

        revision.resolverRevision(dto.getAdminUsuario(), dto.getEstado(), dto.getSugerencia());

        revision.getHecho().setFechaModificacion(LocalDateTime.now());

        hechosRepository.save(revision.getHecho());

        revisionRepository.save(revision);
    }

    private Specification<RevisionHecho> estadoRevision(EstadoRevision estado) {
        return (root, query, cb) ->
                cb.equal(root.get("estado"), estado);
    }
}
