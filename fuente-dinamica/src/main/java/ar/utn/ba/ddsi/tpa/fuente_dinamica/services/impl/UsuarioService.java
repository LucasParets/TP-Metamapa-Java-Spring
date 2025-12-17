package ar.utn.ba.ddsi.tpa.fuente_dinamica.services.impl;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.output.HechoUsuarioDTO;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.Categoria;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.entities.revision.RevisionHecho;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.repositories.IRevisionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UsuarioService {
    private final IHechosRepository hechosRepository;
    private final IRevisionRepository revisionRepository;

    public Page<HechoUsuarioDTO> hechosUsuario(String usuario, Pageable pg) {
        return hechosRepository.findAllByNombreUsuario(usuario, pg).map(this::hechoADTO);
    }


    private HechoUsuarioDTO hechoADTO(Hecho h) {
        RevisionHecho r = revisionRepository.findByHechoId(h.getId());
        HechoUsuarioDTO dto = new HechoUsuarioDTO();
        dto.setIdHecho(h.getId());
        dto.setTitulo(h.getTitulo());
        dto.setDescripcion(h.getDescripcion());
        dto.setCategoria(h.getCategoria().getNombre());
        dto.setFechaHecho(h.getFechaHecho());
        dto.setFechaCarga(h.getFechaCarga());
        dto.setEstadoDelHecho(h.getEstadoDelHecho());
        dto.setEstadoRevision(r.getEstado());
        dto.setFechaRevision(r.getFechaRevision());
        dto.setSugerencia(r.getSugerencia());
        dto.setNombreAdmin(r.getUsuarioAdmin());
        return dto;
    }
}
