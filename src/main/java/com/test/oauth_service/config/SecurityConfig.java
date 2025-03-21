package com.test.oauth_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("secret.header.key")
    private  String SECRET_HEADER;
    @Value("secret.header.value")
    private  String SECRET_VALUE;

    @Bean
    public UserDetailsService userDetailsService() {
        // Crear usuarios en memoria
        UserDetails user = User.builder()
                .username("user")
                .password(bCryptPasswordEncoder().encode("password"))
                .roles("USER")  // Rol USER
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(bCryptPasswordEncoder().encode("admin"))
                .roles("ADMIN")  // Rol ADMIN
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }


    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()  // Permitir acceso sin autenticación
                        .anyRequest().authenticated()  // Proteger cualquier otra ruta
                );
        return http.build();
    }*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("Hola si me estoy ejecutando!");
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(request -> {
                            // Obtener el header enviado
                            String gatewayHeader = request.getHeader(SECRET_HEADER);
                            return gatewayHeader != null && gatewayHeader.equals(SECRET_VALUE);
                        }).permitAll()  // Solo permite si el header es correcto
                        .anyRequest().denyAll() // Bloquea cualquier otro acceso
                );

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}