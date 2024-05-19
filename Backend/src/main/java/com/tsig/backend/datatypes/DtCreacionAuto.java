package com.tsig.backend.datatypes;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DtCreacionAuto {

    private String matricula;

    private double dist_max;

    private Boolean electrico;

    private List<DtCoordenada> recorrido = new ArrayList<>();

    public DtCreacionAuto() {
    }

    public DtCreacionAuto(String matricula, double dist_max, Boolean electrico, List<DtCoordenada> recorrido) {
        this.matricula = matricula;
        this.dist_max = dist_max;
        this.electrico = electrico;
        this.recorrido = recorrido;
    }

}
