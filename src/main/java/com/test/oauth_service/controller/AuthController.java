package com.test.oauth_service.controller;

import com.test.oauth_service.model.JwtResponse;
import com.test.oauth_service.config.JwtUtil;
import com.test.oauth_service.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            log.info("hasta aqui va bien 1");
            log.info("printing user: {}", loginRequest.getUsername());
            log.info("printing pass: {}", loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            log.info("hasta aqui va bien 2");
            // Generar y devolver el token aquí
            String token = new JwtUtil().generateToken(authentication); // Implementa este método
            return ResponseEntity.ok(new JwtResponse(token)); // Asegúrate de que JwtResponse esté definido
        } catch (AuthenticationException e) {
            return ResponseEntity.status(403).body("Credenciales incorrectas"); // Cambia el código de estado a 403
        }
    }
}