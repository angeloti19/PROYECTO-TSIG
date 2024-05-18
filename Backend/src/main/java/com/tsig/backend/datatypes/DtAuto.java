package com.tsig.backend.datatypes;

import org.locationtech.jts.geom.LineString;
import lombok.Data;

@Data
public class DtAuto {

    private Long id;
    private String matricula;
    private double dist_max;
    private LineString recorrido;
    private Boolean electrico;

}
