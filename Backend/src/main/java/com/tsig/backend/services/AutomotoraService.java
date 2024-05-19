package com.tsig.backend.services;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tsig.backend.datatypes.DtCreacionAutomotora;
import com.tsig.backend.entities.Automotora;
import com.tsig.backend.entities.Sucursal;
import com.tsig.backend.exceptions.AutomotoraException;
import com.tsig.backend.repositories.AutomotoraRepository;

@Service
public class AutomotoraService {
    
    @Autowired
    AutomotoraRepository automotoraRepository;

    public ResponseEntity<?> crearAutomotora(DtCreacionAutomotora dtCreacionAutomotora) throws AutomotoraException{

        if(dtCreacionAutomotora.getCoordenadaSucursal() == null){
            throw new AutomotoraException("La automotora "+ dtCreacionAutomotora.getNombreAutomotora()+" debe tener una sucursal");
        }

        Automotora automotora = new Automotora(dtCreacionAutomotora.getNombreAutomotora());

        GeometryFactory Factory = new GeometryFactory(new PrecisionModel(), 32721); //Instanciamos GeometryFactory
        Coordinate coordenadas = new Coordinate(dtCreacionAutomotora.getCoordenadaSucursal().getX(), dtCreacionAutomotora.getCoordenadaSucursal().getY()); //Creamos coordenadas de la sucursal
        Point ubicacionSucursal = Factory.createPoint(coordenadas); //Se crea punto de ubicacion de la sucursal con las coordenadas

        Sucursal sucursal = new Sucursal(dtCreacionAutomotora.getNombreSucursal(),automotora, ubicacionSucursal); // Creacion de sucursal 

        automotora.agregarSucursal(sucursal); //Se añande la sucursal a la lista que tiene automotora

        automotoraRepository.save(automotora); //Guardamos OK

        return ResponseEntity.ok("Automotora "+dtCreacionAutomotora.getNombreAutomotora()+" creada correctamente");

    }
    
}
