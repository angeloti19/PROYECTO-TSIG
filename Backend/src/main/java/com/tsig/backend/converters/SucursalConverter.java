package com.tsig.backend.converters;

import org.springframework.stereotype.Component;

import com.tsig.backend.datatypes.DtCoordenada;
import com.tsig.backend.datatypes.DtSucursal;
import com.tsig.backend.entities.Sucursal;

@Component
public class SucursalConverter {
    
    public DtSucursal toDt(Sucursal sucursal){

        DtCoordenada dtCoordenada = new DtCoordenada(sucursal.getUbicacion().getX(), sucursal.getUbicacion().getY());
        DtSucursal dtSucursal = new DtSucursal(sucursal.getId(), sucursal.getNombre(), dtCoordenada , sucursal.getAutomotora().getId());
        
        return dtSucursal;
    }

}
