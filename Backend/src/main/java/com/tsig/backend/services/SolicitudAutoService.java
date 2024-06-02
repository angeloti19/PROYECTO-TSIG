package com.tsig.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tsig.backend.converters.AutoConverter;
import com.tsig.backend.datatypes.DtSolicitudAuto;
import com.tsig.backend.entities.Auto;
import com.tsig.backend.entities.Automotora;
import com.tsig.backend.entities.Sucursal;
import com.tsig.backend.exceptions.AutoException;
import com.tsig.backend.repositories.AutoRepository;
import com.tsig.backend.repositories.AutomotoraRepository;
import com.tsig.backend.utils.MetodosGeo;

@Service
public class SolicitudAutoService {
    
    @Autowired
    AutoRepository autoRepo;

    @Autowired
    AutoConverter autoConverter;

    @Autowired
    AutomotoraRepository automotoraRepository;

    @Value("${distancia.maxima.destino.sucursal}")
    private Float distanciaMaxDestino;

    MetodosGeo metodosGeo = new MetodosGeo();

    private boolean puntosEnBuffer(Auto auto, Geometry ptoSoli){
        Geometry buffer = metodosGeo.calcularBuffer(auto.getRecorrido(), auto.getDist_max());
        return buffer.contains(ptoSoli);
    }

    private boolean esDestinoValido(Auto auto, Geometry ptoDestino){
        for(Sucursal sucursal : auto.getAutomotora().getSucursales()){
                if(sucursal.getUbicacion().distance(ptoDestino) <= distanciaMaxDestino){
                    return true;
                }
        }
        return false;
    }

    public Auto encontrarAutoMasCercano(List<Auto> autosConCobertura, Geometry puntoSolicitud){
        Auto autoMasCercano = null;
        Double distanciaMinima = Double.MAX_VALUE;

        for(Auto auto: autosConCobertura){
            double distancia = auto.getUbicacion().distance(puntoSolicitud);
            if(distancia < distanciaMinima){
                distanciaMinima = distancia;
                autoMasCercano = auto;
            }
        }
        return autoMasCercano;
    }
    
    public ResponseEntity<?> solicitarAuto(DtSolicitudAuto dtSolicitudAuto) throws AutoException, ParseException{

        //Creamos lista de autos para trabajar con ella y que se vaya actualizando si nos brindan filtros 
        List<Auto> autosDisponibles = new ArrayList<Auto>(); 

        // Si nos mandan filtro de automotora, actualizamos la lista con los autos de esa automotora en especial
        if(dtSolicitudAuto.getIdAutomotora() != null){
            Optional<Automotora> automotoraOpt = automotoraRepository.findById(dtSolicitudAuto.getIdAutomotora());
            Automotora automotora = automotoraOpt.get();
            autosDisponibles = automotora.getAutos(); 
        }else{
            autosDisponibles = autoRepo.findAll(); // Si no, actualizamos la lista con todos los autos disponibles
        }
        
        //Validamos si nos brindan filtro para que actualicemos la lista por autos electricos o de combustion
        if(dtSolicitudAuto.getElectrico() != null){
            Boolean electrico = (dtSolicitudAuto.getElectrico().equals("true"))?true:false;
            autosDisponibles = autosDisponibles.stream()
                                               .filter(auto -> auto.getElectrico() == electrico)
                                               .collect(Collectors.toList());
        }

        //Instanciamos WKTReader para convertir los puntos a geometrias
        WKTReader wktReader = new WKTReader(); 
        // Convertimos los puntos recibidos en formato WKT a geometrias
        Geometry puntoSolicitud = wktReader.read(dtSolicitudAuto.getPtoSolicitud());
        Geometry puntoDestino = wktReader.read(dtSolicitudAuto.getPtoDestino());

        //Actualizamos la lista con los autos, donde su zona de cobertura contengan el punto de solicitud
        autosDisponibles = autosDisponibles.stream()
                                           .filter(auto -> puntosEnBuffer(auto, puntoSolicitud))
                                           .collect(Collectors.toList());
        
        //Por ultimo, actualizamos la lista por los autos que cumplan la condicion del destino valido 
        autosDisponibles = autosDisponibles.stream()
                                           .filter(auto -> esDestinoValido(auto, puntoDestino ))
                                           .collect(Collectors.toList());

        if(autosDisponibles.isEmpty()){
            throw new AutoException("Lo sentimos, no hay autos disponibles que cumplan con sus criterios de búsqueda.");
        }

        Auto autoMasCercano = encontrarAutoMasCercano(autosDisponibles, puntoSolicitud);

        return ResponseEntity.ok(autoConverter.toDto(autoMasCercano));
    }

}
