package com.tsig.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsig.backend.models.UsuarioModel;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    UsuarioModel findByCorreoOrUsuario(String correo, String usuario);
    
}
