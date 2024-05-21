package com.tsig.backend.converters;

import org.springframework.stereotype.Component;

import com.tsig.backend.datatypes.DtAutomotora;
import com.tsig.backend.entities.Auto;
import com.tsig.backend.entities.Automotora;

@Component
public class AutomotoraConverter {
    
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

        DtAutomotora dtAutomotora = new DtAutomotora(automotora.getId(), automotora.getNombre(), cantSucursales, cantAutos, cantAutosElec, cantAutosComb);

        return dtAutomotora;
    }

}
