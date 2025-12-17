package ar.utn.ba.ddsi.tpa.servicio_gateway.configs;


import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebFluxSecurity

public class SecurityConfig {

    @Bean
    SecurityWebFilterChain api(ServerHttpSecurity http,
                               JwtAuthenticationConverter jwtConv) throws Exception {
        ReactiveJwtAuthenticationConverterAdapter reactiveAdapter =
                new ReactiveJwtAuthenticationConverterAdapter(jwtConv);
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers("/actuator/**", "/api/auth/**",
                                "/api/estadistica/**","/api/agregador/public/**",
                                "/api/proxy/metamapa/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/dinamica/hechos/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/dinamica/hechos/**").permitAll()
                        .pathMatchers(HttpMethod.PATCH, "/api/dinamica/hechos/**").authenticated()
                        .pathMatchers(HttpMethod.DELETE, "/api/dinamica/hechos/**").hasRole("ADMIN")
                        .pathMatchers("/api/dinamica/usuario/**").hasRole("USER")
                        .pathMatchers("/api/agregador/admin/**", "/api/dinamica/admin/**",
                                "/api/estatica/**", "/api/proxy/**").hasRole("ADMIN")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(reactiveAdapter)));
        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        var roles = new JwtGrantedAuthoritiesConverter();
        roles.setAuthoritiesClaimName("roles");  // en el access token
        roles.setAuthorityPrefix("ROLE_");       // ADMIN -> ROLE_ADMIN
        var conv = new JwtAuthenticationConverter();
        conv.setJwtGrantedAuthoritiesConverter(roles);
        return conv;
    }

    @Bean
    ReactiveJwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.secret-key}") String secretB64) {
        byte[] k = Decoders.BASE64.decode(secretB64);
        var key = new javax.crypto.spec.SecretKeySpec(k, "HmacSHA256");
        var dec = NimbusReactiveJwtDecoder
                .withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        dec.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(java.time.Duration.ofSeconds(60))));
        return dec;

    }

}