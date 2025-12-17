package ar.utn.ba.ddsi.tpa.fuente_dinamica.controllers;

import ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.output.HechoUsuarioDTO;
import ar.utn.ba.ddsi.tpa.fuente_dinamica.services.impl.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dinamica/usuario")
@AllArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping("/mis-hechos")
    public ResponseEntity<Page<HechoUsuarioDTO>> misHechos(@PageableDefault(sort = "fechaCarga",
                                                                       direction = Sort.Direction.DESC) Pageable pg,
                                                           Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(usuarioService.hechosUsuario(authentication.getName(), pg));
    }
}
