package ar.utn.ba.ddsi.tpa.interfaz_web.config;

import ar.utn.ba.ddsi.tpa.interfaz_web.providers.CustomAuthProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, CustomAuthProvider provider) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(provider)
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/registrarse", "/inicio",
                                "/error", "/colecciones",
                                "/colecciones/**", "/hechos/**", "/hecho/**",
                                "/css/**", "/js/**", "/img/**", "/hechos-destacados").permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/inicio?login", true)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/inicio?logout")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        // Usuario no autenticado → redirigir a login
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect("/login?unauthorized")
                        )
                        // Usuario autenticado pero sin permisos → redirigir a página de error
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendRedirect("/inico?accessDenied")
                        )
                );

        return http.build();
    }
}
