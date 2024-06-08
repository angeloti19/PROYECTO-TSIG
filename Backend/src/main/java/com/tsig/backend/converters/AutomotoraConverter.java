package com.tsig.backend.converters;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tsig.backend.datatypes.DtAutomotora;
import com.tsig.backend.datatypes.DtSucursal;
import com.tsig.backend.entities.Auto;
import com.tsig.backend.entities.Automotora;

@Component
public class AutomotoraConverter {

    @Autowired
    SucursalConverter sucursalConverter;
    
    public DtAutomotora toDt (Automotora automotora){

        int cantSucursales = automotora.getSucursales().size();
        int cantAutos = automotora.getAutos().size();
        int cantAutosElec = 0;
        int cantAutosComb = 0;

        for(Auto auto: automotora.getAutos()){
            if(auto.getElectrico() == true){
                cantAutosElec++;
            }else{
                cantAutosComb++;
            }
        }

        List<DtSucursal> sucursales = automotora.getSucursales()
                                                .stream()
                                                .map(sucursal -> sucursalConverter.toDt(sucursal))
                                                .collect(Collectors.toList());

        DtAutomotora dtAutomotora = new DtAutomotora(automotora.getId(), automotora.getNombre(), cantSucursales, cantAutos, cantAutosElec, cantAutosComb, sucursales);

        return dtAutomotora;
    }

}
