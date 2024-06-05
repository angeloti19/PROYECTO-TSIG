package com.tsig.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tsig.backend.converters.AutomotoraConverter;
import com.tsig.backend.datatypes.DtAutomotora;
import com.tsig.backend.datatypes.DtCoordenada;
import com.tsig.backend.datatypes.DtCreacionAutomotora;
import com.tsig.backend.entities.Automotora;
import com.tsig.backend.entities.Sucursal;
import com.tsig.backend.exceptions.AutomotoraException;
import com.tsig.backend.repositories.AutomotoraRepository;
import com.tsig.backend.utils.MetodosGeo;

@Service
public class AutomotoraService {
    
    @Autowired
    AutomotoraRepository automotoraRepository;

    @Autowired
    AutomotoraConverter automotoraConverter;

    MetodosGeo metodosGeo = new MetodosGeo();

    public DtAutomotora obtenerDtAutomotoraPorId(Long id) throws AutomotoraException{

        Optional<Automotora> automotoraOpt = automotoraRepository.findById(id);
        if(!automotoraOpt.isPresent()){
            throw new AutomotoraException("La automotora ingresada no existe");
        }
        Automotora automotora = automotoraOpt.get();

        DtAutomotora dtAutomotora = automotoraConverter.toDt(automotora);

        return dtAutomotora;
    }

    public List<DtAutomotora> obtenerDtAutomotoras() throws AutomotoraException{
        List<Automotora> automotoras = automotoraRepository.findAll();
        List<DtAutomotora> dtAutomotoras = new ArrayList<DtAutomotora>();
        
        for(Automotora automotora: automotoras){
            dtAutomotoras.add(automotoraConverter.toDt(automotora));
        }
        
        return dtAutomotoras;
    } 

    public ResponseEntity<?> crearAutomotora(DtCreacionAutomotora dtCreacionAutomotora) throws AutomotoraException{
        if(dtCreacionAutomotora.getCoordenadaSucursal() == null){
            throw new AutomotoraException("La automotora "+ dtCreacionAutomotora.getNombreAutomotora()+" debe tener una sucursal");
        }

        Automotora automotora = new Automotora(dtCreacionAutomotora.getNombreAutomotora());
        DtCoordenada dtCoordenada = new DtCoordenada(dtCreacionAutomotora.getCoordenadaSucursal().getX(), dtCreacionAutomotora.getCoordenadaSucursal().getY());
        Point ubicacionSucursal = metodosGeo.crearPunto(dtCoordenada); //Se crea punto de ubicacion de la sucursal con las coordenadas

        Sucursal sucursal = new Sucursal(dtCreacionAutomotora.getNombreSucursal(),automotora, ubicacionSucursal); // Creacion de sucursal 

        automotora.agregarSucursal(sucursal); //Se añande la sucursal a la lista que tiene automotora
        automotoraRepository.save(automotora); //Guardamos OK

        return ResponseEntity.ok("Automotora "+dtCreacionAutomotora.getNombreAutomotora()+" creada correctamente");
    }
    
}
