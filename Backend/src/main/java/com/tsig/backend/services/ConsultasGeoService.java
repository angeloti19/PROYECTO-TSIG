package com.tsig.backend.services;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.util.GeometryCombiner;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsig.backend.entities.Auto;
import com.tsig.backend.repositories.AutoRepository;
import com.tsig.backend.utils.MetodosGeo;

@Service
public class ConsultasGeoService {

    @Autowired
    AutoRepository autoRepository;

    MetodosGeo metodosGeo = new MetodosGeo();
    
    public String zonasSinCobertura(){

        List<Auto> autos = autoRepository.findAll(); // Obtengo los autos en una lista

        String wktMontevideo = autoRepository.geoTextoMontevideo(); //Guardo en una variable string la capa de Montevideo

        WKTReader wktReader = new WKTReader(); // Instancio WKTReader para convertir de texto a geometria la capa de Montevideo
        
        Geometry geometryMontevideo = null;
        try {
            geometryMontevideo = wktReader.read(wktMontevideo);
             // Validar y reparar la geometría
             if (!geometryMontevideo.isValid()) {
                geometryMontevideo = geometryMontevideo.buffer(0);
                if (!geometryMontevideo.isValid()) {
                    throw new RuntimeException("La geometría de Montevideo no pudo ser reparada");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        List<Geometry> listaBuffers = new ArrayList<Geometry>(); //Creo lista de geometrias para almacenar los buffers de cada auto de la automotora

        for(Auto auto: autos){
            Geometry zonaCobertura = metodosGeo.calcularBuffer(auto.getRecorrido(), auto.getDist_max());

            // Validar y reparar cada zona de cobertura
            if (!zonaCobertura.isValid()) {
                zonaCobertura = zonaCobertura.buffer(0);
            }

            listaBuffers.add(zonaCobertura);
        }

        Geometry unionZonaCoberturas = GeometryCombiner.combine(listaBuffers); //Obtengo una sola geometria con los buffer de cada auto.

        // Valido y reparo la union de las zonas de cobertura
        if (!unionZonaCoberturas.isValid()) {
            unionZonaCoberturas = unionZonaCoberturas.buffer(0);
        }

        //Obtengo las zonas sin cobertura haciendo una diferencia 
        Geometry zonasSinCobertura = geometryMontevideo.difference(unionZonaCoberturas);

        //Valido y reparo las zonas sin cobertura
        if (!zonasSinCobertura.isValid()) {
            zonasSinCobertura = zonasSinCobertura.buffer(0);
        }

        // Devuelvo en formato wkt 
        return zonasSinCobertura.toText();

    }

}
