package com.tsig.backend.datatypes;

import lombok.Data;

@Data
public class DtAutomotora {
    
    private Long id;
    private String nombre;
    
    public DtAutomotora() {
    }

    public DtAutomotora(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

}
