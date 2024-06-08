package com.tsig.backend.datatypes;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DtAutomotora {
    
    private Long id;
    private String nombre;
    private int cantSucursales;
    private int cantAutosTotal;
    private int cantAutosElec;
    private int cantAutosComb;
    private List<DtSucursal> sucursales = new ArrayList<DtSucursal>();
    
    public DtAutomotora() {
    }

    public DtAutomotora(Long id, String nombre, int cantSucursales, int cantAutosTotal, int cantAutosElec,
            int cantAutosComb, List<DtSucursal> sucursales) {
        this.id = id;
        this.nombre = nombre;
        this.cantSucursales = cantSucursales;
        this.cantAutosTotal = cantAutosTotal;
        this.cantAutosElec = cantAutosElec;
        this.cantAutosComb = cantAutosComb;
        this.sucursales = sucursales;
    }

    

    

}
