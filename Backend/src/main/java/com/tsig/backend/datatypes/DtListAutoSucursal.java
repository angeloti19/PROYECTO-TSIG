package com.tsig.backend.datatypes;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DtListAutoSucursal {

    private List<DtAuto> autos = new ArrayList<DtAuto>();
    private List<DtSucursal> sucursales = new ArrayList<DtSucursal>();
    
    public DtListAutoSucursal() {
    }

    public DtListAutoSucursal(List<DtAuto> autos, List<DtSucursal> sucursales) {
        this.autos = autos;
        this.sucursales = sucursales;
    }

}
