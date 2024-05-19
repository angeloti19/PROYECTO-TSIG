package com.tsig.backend.services;

import java.util.Optional;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tsig.backend.entities.Auto;
import com.tsig.backend.entities.Automotora;
import com.tsig.backend.exceptions.AutoException;
import com.tsig.backend.repositories.AutoRepository;
import com.tsig.backend.repositories.AutomotoraRepository;
import com.tsig.backend.converters.AutoConverter;
import com.tsig.backend.datatypes.DtAuto;

@Service
public class AutoService {

    @Autowired
    AutoRepository autoRepo;

    @Autowired
    AutoConverter autoConverter;

    @Autowired
    AutomotoraRepository automotoraRepository;

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

    public ResponseEntity<?> crearAuto(DtAuto dtAuto) throws AutoException {

        if(dtAuto.getIdAutomotora() == null){
            throw new AutoException("Debe asignarse a una automotora");
        }

        Optional<Automotora> automotora = automotoraRepository.findById(dtAuto.getIdAutomotora());
        if(!automotora.isPresent()){
            throw new AutoException("La automotora ingresada no existe");
        }

        GeometryFactory Factory = new GeometryFactory(new PrecisionModel(), 32721);
        Coordinate[] coordinate = dtAuto.getRecorrido().stream()
                                                       .map(coord -> new Coordinate(coord.getX(), coord.getX()))
                                                       .toArray(Coordinate[]::new);
        LineString recorridoAuto =  Factory.createLineString(coordinate);

        Automotora automotoraEntidad = automotora.get();

        Auto auto = new Auto(dtAuto.getMatricula(), dtAuto.getDist_max(), recorridoAuto, dtAuto.getElectrico(), automotoraEntidad);

        automotoraEntidad.agregarAuto(auto);

        automotoraRepository.save(automotoraEntidad);

        return ResponseEntity.ok("Auto creado correctamente!!");

    }
}
