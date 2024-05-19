package com.tsig.backend.datatypes;


import lombok.Data;

@Data
public class DtCreacionAutomotora {
    
    private String nombreAutomotora;

    private String nombreSucursal;

    private DtCoordenada coordenadaSucursal;

    public DtCreacionAutomotora(String nombreAutomotora, String nombreSucursal, DtCoordenada coordenadaSucursal) {
        this.nombreAutomotora = nombreAutomotora;
        this.nombreSucursal = nombreSucursal;
        this.coordenadaSucursal = coordenadaSucursal;
    }

    public DtCreacionAutomotora() {
    }    

    


}
