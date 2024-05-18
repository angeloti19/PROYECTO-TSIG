package com.tsig.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsig.backend.entities.Auto;
import com.tsig.backend.exceptions.AutoException;
import com.tsig.backend.repositories.AutoRepository;
import com.tsig.backend.converters.AutoConverter;
import com.tsig.backend.datatypes.DtAuto;

@Service
public class AutoService {

    @Autowired
    private AutoRepository autoRepo;

    @Autowired
    private AutoConverter autoConverter;

    public Auto guardarAuto(DtAuto autoDto) throws AutoException {
        try {
            // Convertir el DTO a modelo
            Auto auto = autoConverter.toModel(autoDto);
            // Guardar el modelo en el repositorio y retornar el objeto guardado
            return autoRepo.save(auto);
        } catch (Exception e) {
            throw new AutoException("Error al guardar el auto: " + e.getMessage());
        }
    }
}
