package com.tsig.backend.datatypes;

import lombok.Data;

@Data
public class DtSucursal {
    
    private Long id;
    private String nombre;
    private DtCoordenada coordenadas;
    private Long idAutomotora;

    public DtSucursal() {
    }

    public DtSucursal(Long id, String nombre, DtCoordenada coordenadas, Long idAutomotora) {
        this.id = id;
        this.nombre = nombre;
        this.coordenadas = coordenadas;
        this.idAutomotora = idAutomotora;
    }

}
