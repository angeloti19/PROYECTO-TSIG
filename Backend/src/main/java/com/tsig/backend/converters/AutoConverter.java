package com.tsig.backend.converters;

import org.springframework.stereotype.Component;
import com.tsig.backend.dto.AutoDto;
import com.tsig.backend.models.AutoModel;

@Component
public class AutoConverter {

    public AutoModel toModel(AutoDto autoDto) {
        AutoModel auto = new AutoModel();
        auto.setMatricula(autoDto.getMatricula());
        auto.setDist_max(autoDto.getDist_max());
        auto.setRecorrido(autoDto.getRecorrido());
        auto.setElectrico(autoDto.getElectrico());
        return auto;
    }

    public AutoDto toDto(AutoModel autoModel) {
        AutoDto autoDto = new AutoDto();

        autoDto.setId(autoModel.getId());
        autoDto.setMatricula(autoModel.getMatricula());
        autoDto.setDist_max(autoModel.getDist_max());
        autoDto.setElectrico(autoModel.getElectrico());
        autoDto.setRecorrido(autoModel.getRecorrido());

        return autoDto;
    }

}
