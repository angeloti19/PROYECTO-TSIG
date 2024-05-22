package com.tsig.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
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
import com.tsig.backend.datatypes.DtCoordenada;

@Service
public class AutoService {

    @Autowired
    AutoRepository autoRepo;

    @Autowired
    AutoConverter autoConverter;

    @Autowired
    AutomotoraRepository automotoraRepository;

    public List<DtAuto> listarAutos(Long idAutomotora) throws AutoException{

        Optional<Automotora> automotora = automotoraRepository.findById(idAutomotora);
        if(!automotora.isPresent()){
            throw new AutoException("La automotora ingresada no existe");
        }

        Automotora automotoraEntidad = automotora.get();

        List<Auto> autos = automotoraEntidad.getAutos();
        
        List<DtAuto> dtAutos = new ArrayList<DtAuto>();

        for(Auto auto: autos){
            dtAutos.add(autoConverter.toDto(auto));
        }

        return dtAutos;

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
        Coordinate[] coordinate = dtAuto.getRecorrido().stream() //Convierto la lista de DtCoordenadas en un arreglo de Coordinate para crear la LINEA
                                                       .map(coord -> new Coordinate(coord.getX(), coord.getY()))
                                                       .toArray(Coordinate[]::new);
        LineString recorridoAuto =  Factory.createLineString(coordinate);


        DtCoordenada primerCoordenada = dtAuto.getRecorrido().get(0); //Obtengo la primer coordenada del recorrido
        Coordinate coordenadaUbiAuto = new Coordinate(primerCoordenada.getX(), primerCoordenada.getY()); // Creo las coordenadas para el pto de ubicacion del auto
        Point ubiAuto = Factory.createPoint(coordenadaUbiAuto); //Se crea el punto de ubicacion del auto

        Automotora automotoraEntidad = automotora.get(); //Obtengo a la automotora como entidad 

        Auto auto = new Auto(dtAuto.getMatricula(), dtAuto.getDist_max(), recorridoAuto, dtAuto.getElectrico(), ubiAuto ,automotoraEntidad);

        automotoraEntidad.agregarAuto(auto);

        automotoraRepository.save(automotoraEntidad); 

        return ResponseEntity.ok("Auto creado correctamente!!");

    }
}
