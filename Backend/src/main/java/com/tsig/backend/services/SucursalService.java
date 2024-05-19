package com.tsig.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tsig.backend.converters.SucursalConverter;
import com.tsig.backend.datatypes.DtSucursal;
import com.tsig.backend.entities.Automotora;
import com.tsig.backend.entities.Sucursal;
import com.tsig.backend.exceptions.SucursalException;
import com.tsig.backend.repositories.AutomotoraRepository;
import com.tsig.backend.repositories.SucursalRepository;

@Service
public class SucursalService {
    
    @Autowired
    SucursalRepository sucursalRepository;

    @Autowired
    AutomotoraRepository automotoraRepository;

    @Autowired
    SucursalConverter sucursalConverter;

    public List<DtSucursal> listarSucursales(Long id) throws SucursalException{

        Optional<Automotora> automotora = automotoraRepository.findById(id);
        if(!automotora.isPresent()){
            throw new SucursalException("La automotora ingresada no existe");
        }

        Automotora automotoraEntidad = automotora.get();

        List<Sucursal> sucursales = automotoraEntidad.getSucursales();

        List<DtSucursal> dtSucursales = new ArrayList<DtSucursal>();

        for(Sucursal sucursal: sucursales){
            dtSucursales.add(sucursalConverter.toDt(sucursal));
        }

        return dtSucursales;
    }

    public ResponseEntity<?> crearSucursal(DtSucursal dtSucursal) throws SucursalException{

        if(dtSucursal.getIdAutomotora() == null || dtSucursal.getNombre() == null || dtSucursal.getCoordenadas() == null){
            throw new SucursalException("Faltan datos para la sucursal");
        }

        Optional<Automotora> automotora = automotoraRepository.findById(dtSucursal.getIdAutomotora()); // Buscamos a la automotora por su ID
        if(!automotora.isPresent()){
            throw new SucursalException("La automotora ingresada no existe");
        }

        GeometryFactory Factory = new GeometryFactory(new PrecisionModel(), 32721);
        Coordinate coordenadas = new Coordinate(dtSucursal.getCoordenadas().getX(), dtSucursal.getCoordenadas().getY());
        Point ubicacionSucursal = Factory.createPoint(coordenadas);

        Automotora automotoraEnt = automotora.get(); //Obtenemos la automotora como entidad con la funcion .get()

        Sucursal sucursal = new Sucursal(dtSucursal.getNombre(), automotoraEnt , ubicacionSucursal); // Creamos sucursal 

        automotoraEnt.agregarSucursal(sucursal);

        automotoraRepository.save(automotoraEnt); // Actualizamos automotora con su nueva sucursal creada. 

        return ResponseEntity.ok("Sucursal "+ dtSucursal.getNombre()+" creada correctamente");
    }

}
