package com.tsig.backend.dto;

import lombok.Data;

@Data //Este arroba genera los getters y setter automaticamente
public class UsuarioDto {
    
    private Long id;

    private String nombre;

    private String usuario;

    private String correo;

    private String contrasenia;



}
