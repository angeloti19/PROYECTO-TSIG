package com.tsig.backend.datatypes;

import lombok.Data;

@Data
public class DtCoordenada {
    
    private double x;
    private double y;
    
    public DtCoordenada() {
    }

    public DtCoordenada(double x, double y) {
        this.x = x;
        this.y = y;
    }

}
