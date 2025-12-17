package ar.utn.ba.ddsi.tpa.servicio_auth.repositories;

import ar.utn.ba.ddsi.tpa.servicio_auth.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNombreDeUsuario(String nombreDeUsuario);
    boolean existsByNombreDeUsuario(String nombreDeUsuario);
    boolean existsByEmail(String email);
}
