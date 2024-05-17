package com.tsig.backend.dto;

import org.locationtech.jts.geom.LineString;
import lombok.Data;

@Data
public class AutoDto {

    private Long id;
    private String matricula;
    private double dist_max;
    private LineString recorrido;
    private Boolean electrico;

}
