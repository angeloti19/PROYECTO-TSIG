package com.tsig.backend.datatypes;

import lombok.Data;

@Data
public class DtAutoExtendido {

    private DtAuto auto;
    private DtCoordenada puntoLevante;

    public DtAutoExtendido(){}

    public DtAutoExtendido(DtAuto auto, DtCoordenada puntoLevante){
        this.auto = auto;
        this.puntoLevante = puntoLevante;
    }
}
