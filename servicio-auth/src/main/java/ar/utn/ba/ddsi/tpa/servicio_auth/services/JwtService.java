package ar.utn.ba.ddsi.tpa.servicio_auth.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    @Getter
    private final Key key;

    private static final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000; // 15 min
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 días

    public JwtService(@Value("${jwt.key}") String secretB64) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretB64));
    }

    public String generarAccessToken(String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("metamapa-server")
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .claim("roles", List.of(rol))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generarRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("metamapa-server")
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .claim("type", "refresh") // diferenciamos refresh del access
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validarToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
