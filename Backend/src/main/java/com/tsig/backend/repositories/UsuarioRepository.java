package com.tsig.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsig.backend.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByCorreo(String correo);
    
}
