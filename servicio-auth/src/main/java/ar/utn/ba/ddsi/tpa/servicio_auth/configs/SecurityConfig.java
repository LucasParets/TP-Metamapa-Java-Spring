package ar.utn.ba.ddsi.tpa.servicio_auth.configs;

import ar.utn.ba.ddsi.tpa.servicio_auth.filters.JwtAuthenticationFilter;
import ar.utn.ba.ddsi.tpa.servicio_auth.services.JwtService;
import ar.utn.ba.ddsi.tpa.servicio_auth.services.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginService loginService, JwtService jwtService) throws Exception {
        System.out.println("=== CONFIGURANDO SECURITY ===");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/auth/login", "/api/auth/refresh", "/api/auth/register", "/actuator/**").permitAll();
                    auth.requestMatchers("/api/auth/user/roles").authenticated();
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(new JwtAuthenticationFilter(loginService, jwtService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
