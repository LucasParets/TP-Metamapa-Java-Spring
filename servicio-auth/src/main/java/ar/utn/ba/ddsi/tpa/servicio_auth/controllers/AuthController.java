package ar.utn.ba.ddsi.tpa.servicio_auth.controllers;

import ar.utn.ba.ddsi.tpa.servicio_auth.models.dtos.*;
import ar.utn.ba.ddsi.tpa.servicio_auth.exceptions.NotFoundException;
import ar.utn.ba.ddsi.tpa.servicio_auth.services.JwtService;
import ar.utn.ba.ddsi.tpa.servicio_auth.services.LoginService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final LoginService loginService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginApi(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            // Validación básica de credenciales
            if (username == null || username.trim().isEmpty() ||
                    password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Autenticar usuario usando el LoginService
            loginService.autenticarUsuario(username, password);

            // Generar tokens
            String accessToken = loginService.generarAccessToken(username);
            String refreshToken = loginService.generarRefreshToken(username);

            AuthResponseDTO response = AuthResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            log.info("El usuario {} está logueado. El token generado es {}", username, accessToken);

            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request) {
        try {
            String username = jwtService.validarToken(request.getRefreshToken());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtService.getKey())
                    .build()
                    .parseClaimsJws(request.getRefreshToken())
                    .getBody();

            if (!"refresh".equals(claims.get("type"))) {
                return ResponseEntity.badRequest().build();
            }

            String newAccessToken = loginService.generarAccessToken(username);
//            String newAccessToken = JwtUtil.generarAccessToken(username);
            TokenResponse response = new TokenResponse(newAccessToken, request.getRefreshToken());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/roles")
    public ResponseEntity<UserRolesDTO> getUserRoles(Authentication authentication) {
        try {
            String username = authentication.getName();
            UserRolesDTO response = loginService.obtenerRoles(username);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Usuario no encontrado", e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener roles y permisos del usuario", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponseDTO tokens = loginService.registrar(request);
            // 201 Created y tokens para que el frontend ya quede logueado
            return ResponseEntity.status(HttpStatus.CREATED).body(tokens);
        } catch (IllegalArgumentException e) {
            log.warn("Registro inválido: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409 por unicidad
        } catch (Exception e) {
            log.error("Error al registrar usuario", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo registrar el usuario");
        }
    }
}
