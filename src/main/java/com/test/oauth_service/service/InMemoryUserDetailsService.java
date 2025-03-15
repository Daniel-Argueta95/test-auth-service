package com.test.oauth_service.service;



import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryUserDetailsService implements UserDetailsService {

    private final List<User> users = new ArrayList<>(); // Lista de usuarios

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        users.add(new User("user1", passwordEncoder.encode("pass"), new ArrayList<>())); // Usuario 1
        users.add(new User("user2", passwordEncoder.encode("pass"), new ArrayList<>())); // Usuario 2

        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}



