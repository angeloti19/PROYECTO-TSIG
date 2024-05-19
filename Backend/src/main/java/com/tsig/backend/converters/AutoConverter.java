package com.tsig.backend.converters;

import org.springframework.stereotype.Component;

import com.tsig.backend.datatypes.DtAuto;
import com.tsig.backend.entities.Auto;

@Component
public class AutoConverter {

    public Auto toModel(DtAuto autoDto) {
        Auto auto = new Auto();
        auto.setMatricula(autoDto.getMatricula());
        auto.setDist_max(autoDto.getDist_max());
        auto.setElectrico(autoDto.getElectrico());
        return auto;
    }

    public DtAuto toDto(Auto autoModel) {
        DtAuto autoDto = new DtAuto();

        autoDto.setMatricula(autoModel.getMatricula());
        autoDto.setDist_max(autoModel.getDist_max());
        autoDto.setElectrico(autoModel.getElectrico());

        return autoDto;
    }

}
