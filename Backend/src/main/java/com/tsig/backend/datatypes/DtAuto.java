package com.tsig.backend.datatypes;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class DtAuto {

    private String matricula;

    private double dist_max;

    private Boolean electrico;

    private Long idAutomotora;

    private List<DtCoordenada> recorrido = new ArrayList<>();

    public DtAuto() {
    }

    public DtAuto(String matricula, double dist_max, Boolean electrico, Long idAutomotora,
            List<DtCoordenada> recorrido) {
        this.matricula = matricula;
        this.dist_max = dist_max;
        this.electrico = electrico;
        this.idAutomotora = idAutomotora;
        this.recorrido = recorrido;
    }

    

}
