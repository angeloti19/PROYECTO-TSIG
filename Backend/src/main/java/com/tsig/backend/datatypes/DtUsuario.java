package com.tsig.backend.datatypes;

import lombok.Data;

@Data //Este arroba genera los getters y setter automaticamente
public class DtUsuario {
    
    private Long id;

    private String nombre;

    private String correo;

    private String contrasenia;
}
