package com.test.oauth_service.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
@Slf4j
@Component
public class JwtUtil {

    private static final String SECRET_KEY = "Clave25"; // Cambia esto por tu clave secreta
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 horas

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        // Crear claims
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", "user"); // Ejemplo de claim adicional

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}