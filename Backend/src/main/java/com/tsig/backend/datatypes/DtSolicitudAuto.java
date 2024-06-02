package com.tsig.backend.datatypes;

import lombok.Data;

@Data
public class DtSolicitudAuto {

    private String ptoSolicitud;
    private String ptoDestino;
    private Long idAutomotora;
    private String electrico;
    private Float dist_max;

    public DtSolicitudAuto() {
    }

    public DtSolicitudAuto(String ptoSolicitud, String ptoDestino, Long idAutomotora, String electrico,
                Float dist_max) {
        this.ptoSolicitud = ptoSolicitud;
        this.ptoDestino = ptoDestino;
        this.idAutomotora = idAutomotora;
        this.electrico = electrico;
        this.dist_max = dist_max;
    }
}
