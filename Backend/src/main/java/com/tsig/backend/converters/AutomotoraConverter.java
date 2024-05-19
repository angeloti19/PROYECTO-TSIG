package com.tsig.backend.converters;

import org.springframework.stereotype.Component;

import com.tsig.backend.datatypes.DtAutomotora;
import com.tsig.backend.entities.Automotora;

@Component
public class AutomotoraConverter {
    
    public DtAutomotora toDt (Automotora automotora){
        DtAutomotora dtAutomotora = new DtAutomotora(automotora.getId(), automotora.getNombre());
        return dtAutomotora;
    }

}
