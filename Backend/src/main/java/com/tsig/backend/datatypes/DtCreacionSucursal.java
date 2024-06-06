package com.tsig.backend.datatypes;

import lombok.Data;

@Data
public class DtCreacionSucursal {

    private String nombre;
    private DtCoordenada coordenadas;

    public DtCreacionSucursal() {
    }
    
    public DtCreacionSucursal(String nombre, DtCoordenada coordenadas) {
        this.nombre = nombre;
        this.coordenadas = coordenadas;
    }

}
