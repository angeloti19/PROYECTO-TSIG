package com.tsig.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "usuarios")
public class UsuarioModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "correo")
    private String correo;

    @Column(name = "contrasenia")
    private String contrasenia;

}
