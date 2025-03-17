package com.test.oauth_service.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(UserDetails userDetails) {
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        System.out.println("printing roles");
        System.out.println(roles);

        // Crear el token JWT
        return Jwts.builder()
                .subject(userDetails.getUsername())  // Asignar el sujeto (username)
                .claim("roles", roles)  // Incluir roles en el token
                .issuedAt(new Date())  // Fecha de emisión
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))  // Fecha de expiración
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // Firmar con la clave secreta
                .compact();  // Convertir a String
    }

    public boolean validateToken(String token) {
        try {
            // Validar el token
            Jwts.parser()
                    .verifyWith(getSigningKey())  // Verificar con la clave secreta
                    .build()
                    .parseSignedClaims(token);  // Parsear y validar el token
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        // Obtener los claims (datos) del token
        return Jwts.parser()
                .verifyWith(getSigningKey())  // Verificar con la clave secreta
                .build()
                .parseSignedClaims(token)  // Parsear el token
                .getPayload();  // Obtener los claims
    }

    private SecretKey getSigningKey() {
        // Convertir la clave secreta en un objeto SecretKey
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
}
}