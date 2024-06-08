package com.tsig.backend.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tsig.backend.enums.ERole;
import com.tsig.backend.jwt.JwtService;
import com.tsig.backend.controllers.AuthResponse;
import com.tsig.backend.controllers.LoginRequest;
import com.tsig.backend.controllers.RegisterRequest;
import com.tsig.backend.entities.Usuario;
import com.tsig.backend.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getAuthorities());
        
        String token = jwtService.getTokenv2(extraClaims, user);
        
        return AuthResponse.builder()
            .message("User successfully logged in")
            .token(token)
            .username(user.getUsername())
            .role(user.getAuthorities().toString())
            .build();

    }

    public AuthResponse register(RegisterRequest request) {
        ERole[] rolValues = ERole.values(); // 0 = ROLE_USER | 1 = ROLE_ADMIN
        Usuario user = Usuario.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword())) // encrypted password
            .correo(request.getCorreo())
            .role(rolValues[request.getRole()])
            .build();
    
        Usuario savedUser = userRepository.save(user);
        
        if (savedUser == null || userRepository.findById(savedUser.getId()).isEmpty()) {
            throw new RuntimeException("User registration failed. Please try again.");
        } else {
            return AuthResponse.builder()
                .message("Usuario registrado correctamente!")
                .token(jwtService.getToken(user))
                .username(user.getUsername())
                .role(user.getRole().toString())
                .build(); 
        }
    }
}
