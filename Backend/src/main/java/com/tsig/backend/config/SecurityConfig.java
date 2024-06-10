package com.tsig.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tsig.backend.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        return http
            .csrf(csrf->csrf.disable())                                 // No se utiliza este tipo de token
            .authorizeHttpRequests(
                authRequest->authRequest
                .requestMatchers("/auth/**").permitAll()    // End points -> Registro y Login
                .requestMatchers(
                    "/api/automotora", 
                    "/api/automotora/{atmId}",
                    "/api/consultas/top10Automotoras",
                    "/api/autoSolicitud/**", 
                    "/api/autoSucursalCercanos/**"
                    ).permitAll()                           // End points -> Usuario anonimo
                .requestMatchers(
                    "/swagger-ui/**", 
                                "/v3/api-docs/**"
                                ).permitAll()                           // End Points -> Swagger
                .anyRequest().authenticated())                          // End points -> Administrador
            .sessionManagement(
                sessionManager->
                sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No se utilizan sesiones
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();         
    }
}
