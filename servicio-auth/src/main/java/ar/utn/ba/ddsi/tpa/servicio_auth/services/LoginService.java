package ar.utn.ba.ddsi.tpa.servicio_auth.services;

import ar.utn.ba.ddsi.tpa.servicio_auth.exceptions.NotFoundException;
import ar.utn.ba.ddsi.tpa.servicio_auth.models.dtos.AuthResponseDTO;
import ar.utn.ba.ddsi.tpa.servicio_auth.models.dtos.RegisterRequest;
import ar.utn.ba.ddsi.tpa.servicio_auth.models.dtos.UserRolesDTO;
import ar.utn.ba.ddsi.tpa.servicio_auth.models.entities.Rol;
import ar.utn.ba.ddsi.tpa.servicio_auth.models.entities.Usuario;
import ar.utn.ba.ddsi.tpa.servicio_auth.repositories.UsuariosRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LoginService {
    private final UsuariosRepository usuariosRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginService(UsuariosRepository usuariosRepository, JwtService jwtService) {
        this.usuariosRepository = usuariosRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Usuario autenticarUsuario(String username, String password) {
        Optional<Usuario> usuarioOpt = usuariosRepository.findByNombreDeUsuario(username);

        if (usuarioOpt.isEmpty()) {
            throw new NotFoundException("Usuario", username);
        }

        Usuario usuario = usuarioOpt.get();

        // Verificar la contraseña usando BCrypt
        if (!passwordEncoder.matches(password, usuario.getContrasenia())) {
            throw new NotFoundException("Usuario", username);
        }

        return usuario;
    }

    @Transactional
    public AuthResponseDTO registrar(RegisterRequest req) {

        if (usuariosRepository.existsByNombreDeUsuario(req.getNombreDeUsuario())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        if (usuariosRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        Usuario nuevo = new Usuario();
        nuevo.setNombre(req.getNombre());
        nuevo.setApellido(req.getApellido());
        nuevo.setNombreDeUsuario(req.getNombreDeUsuario());
        nuevo.setEmail(req.getEmail());
        nuevo.setContrasenia(passwordEncoder.encode(req.getContrasenia()));
        nuevo.setRol(Rol.USER);

        usuariosRepository.save(nuevo);

        String access = jwtService.generarAccessToken(nuevo.getNombreDeUsuario(), "USER");
        String refresh = jwtService.generarRefreshToken(nuevo.getNombreDeUsuario());

        return AuthResponseDTO.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .build();
    }

    public String generarAccessToken(String username) {
        Usuario usuario = this.obtenerUsuario(username);
        return jwtService.generarAccessToken(username, usuario.getRol().name());
    }

    public String generarRefreshToken(String username) {
        return jwtService.generarRefreshToken(username);
    }

    public Usuario obtenerUsuario(String username) {
        Optional<Usuario> usuarioOpt = usuariosRepository.findByNombreDeUsuario(username);

        if (usuarioOpt.isEmpty()) {
            throw new NotFoundException("Usuario", username);
        }

        return usuarioOpt.get();
    }

    public UserRolesDTO obtenerRoles(String username) {
        Usuario usuario = this.obtenerUsuario(username);

        return UserRolesDTO.builder()
                .username(usuario.getNombreDeUsuario())
                .rol(usuario.getRol())
                .build();
    }
}
