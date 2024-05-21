package com.tsig.backend.converters;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Component;

import com.tsig.backend.datatypes.DtAuto;
import com.tsig.backend.datatypes.DtCoordenada;
import com.tsig.backend.entities.Auto;

@Component
public class AutoConverter {

    public Auto toEntity(DtAuto dtAuto) {
        Auto auto = new Auto();
        auto.setMatricula(dtAuto.getMatricula());
        auto.setDist_max(dtAuto.getDist_max());
        auto.setElectrico(dtAuto.getElectrico());
        return auto;
    }

    public DtAuto toDto(Auto auto) {

        Coordinate[] coordenadaArray = auto.getRecorrido().getCoordinates(); // Obtengo el recorrido del auto en un arreglo de coordenadas

        List<DtCoordenada> dtCoordenadas = new ArrayList<DtCoordenada>(); // Creo una nueva lista de DtCoordenada

        for(Coordinate coordinate: coordenadaArray){ //Recorro el arreglo de coordenadas del recorrido del auto
            DtCoordenada dtCoordenada = new DtCoordenada(coordinate.getX(), coordinate.getY());
            dtCoordenadas.add(dtCoordenada);
        }

        DtAuto dtAuto = new DtAuto(auto.getMatricula(), auto.getDist_max(), auto.getElectrico(), auto.getAutomotora().getId(), dtCoordenadas);

        return dtAuto;
    }

}
