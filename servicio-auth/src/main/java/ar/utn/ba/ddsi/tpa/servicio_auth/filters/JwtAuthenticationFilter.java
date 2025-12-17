package ar.utn.ba.ddsi.tpa.servicio_auth.filters;

import ar.utn.ba.ddsi.tpa.servicio_auth.models.entities.Rol;
import ar.utn.ba.ddsi.tpa.servicio_auth.services.JwtService;
import ar.utn.ba.ddsi.tpa.servicio_auth.services.LoginService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final LoginService loginService;
    private final JwtService jwtService;

    public JwtAuthenticationFilter(LoginService loginService, JwtService jwtService) {
        this.loginService = loginService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String username = jwtService.validarToken(token);
                String rol = loginService.obtenerUsuario(username).getRol().name();
                var auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            }
        } else {
            System.out.println("No hay token de autorización");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // No aplicar el filtro JWT solo a los endpoints públicos de autenticación
        return path.equals("/api/auth/login") || path.equals("/api/auth/refresh")
                || path.equals("/api/auth/register");
    }
}
