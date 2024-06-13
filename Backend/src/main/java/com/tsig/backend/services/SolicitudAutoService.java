package com.tsig.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.util.GeometryCombiner;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tsig.backend.converters.AutoConverter;
import com.tsig.backend.converters.SucursalConverter;
import com.tsig.backend.datatypes.DtAuto;
import com.tsig.backend.datatypes.DtAutoExtendido;
import com.tsig.backend.datatypes.DtCoordenada;
import com.tsig.backend.datatypes.DtListAutoSucursal;
import com.tsig.backend.datatypes.DtSolicitudAuto;
import com.tsig.backend.datatypes.DtSucursal;
import com.tsig.backend.entities.Auto;
import com.tsig.backend.entities.Automotora;
import com.tsig.backend.entities.Sucursal;
import com.tsig.backend.exceptions.AutoException;
import com.tsig.backend.repositories.AutoRepository;
import com.tsig.backend.repositories.AutomotoraRepository;
import com.tsig.backend.repositories.SucursalRepository;
import com.tsig.backend.utils.MetodosGeo;

@Service
public class SolicitudAutoService {
    
    @Autowired
    AutoRepository autoRepo;

    @Autowired
    AutoConverter autoConverter;

    @Autowired
    SucursalConverter sucursalConverter;

    @Autowired
    AutomotoraRepository automotoraRepository;

    @Autowired
    SucursalRepository sucursalRepository;

    @Value("${distancia.maxima.destino.sucursal}")
    private Float distanciaMaxDestino;

    MetodosGeo metodosGeo = new MetodosGeo();

    private boolean puntosEnBuffer(Auto auto, Geometry ptoSoli){
        Geometry buffer = metodosGeo.calcularBuffer(auto.getRecorrido(), auto.getDist_max());
        return buffer.contains(ptoSoli);
    }

    private boolean bufferConDistMax(Auto auto, Geometry ptoSoli, Float dist_max){
        Geometry buffer = metodosGeo.calcularBuffer(auto.getRecorrido(), (auto.getDist_max()+dist_max));
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


        if(dtSolicitudAuto.getDist_max() == null){
            //Actualizamos la lista con los autos, donde su zona de cobertura contengan el punto de solicitud
            autosDisponibles = autosDisponibles.stream()
                                               .filter(auto -> puntosEnBuffer(auto, puntoSolicitud))
                                               .collect(Collectors.toList());

        }else{
            //Actualizamos la lista con los autos, donde su zona de cobertura + dist_max contengan el punto de solicitud
            autosDisponibles = autosDisponibles.stream()
                                               .filter(auto -> bufferConDistMax(auto, puntoSolicitud, dtSolicitudAuto.getDist_max()))
                                               .collect(Collectors.toList());
        }
        
        //Por ultimo, actualizamos la lista por los autos que cumplan la condicion del destino valido 
        autosDisponibles = autosDisponibles.stream()
                                           .filter(auto -> esDestinoValido(auto, puntoDestino ))
                                           .collect(Collectors.toList());

        if(autosDisponibles.isEmpty()){
            throw new AutoException("Lo sentimos, no hay autos disponibles que cumplan con sus criterios de búsqueda.");
        }

        Auto autoMasCercano = encontrarAutoMasCercano(autosDisponibles, puntoSolicitud);
        if(puntosEnBuffer(autoMasCercano, puntoSolicitud)){
            //Caso comun, la zona de cobertura del auto ya cubre el punto de solicitud
            //Por conveniencia se envia con datatype extendido con punto de levante nulo
            DtAutoExtendido auto = new DtAutoExtendido(autoConverter.toDto(autoMasCercano), null);
            return ResponseEntity.ok(auto);
        }else{
            //La zona de cobertura del auto no cubre el punto de solicitud, se necesita retornar un punto de levante al que pueda acceder
            
            //Se obtiene la geometria de calles
            String wktCalle = autoRepo.geoTextoCapaCalle();
            Geometry geometryCalle = null;
            try {
                geometryCalle = wktReader.read(wktCalle);
                // Validar y reparar la geometría
                if (!geometryCalle.isValid()) {
                    geometryCalle = geometryCalle.buffer(0);
                    if (!geometryCalle.isValid()) {
                        throw new RuntimeException("La geometría de calles no pudo ser reparada");
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //Se obtiene el borde de la zona de cobertura
            Geometry buffer = metodosGeo.calcularBuffer(autoMasCercano.getRecorrido(), autoMasCercano.getDist_max());
            Geometry bordeBuffer = buffer.getBoundary();

            //Se obtiene los puntos/punto de su interceccion
            Geometry interseccion = bordeBuffer.intersection(geometryCalle);

            Coordinate coordenadaLevante = null;
            if(interseccion instanceof Point){
                //Si es solo un punto
                Point puntoLevanteGeom = (Point) interseccion;
                coordenadaLevante = puntoLevanteGeom.getCoordinate();
            }else if(interseccion instanceof MultiPoint){
                //Si son multiples puntos
                MultiPoint puntos = (MultiPoint) interseccion;
                Coordinate[] coordenadas = puntos.getCoordinates();

                //Se busca la coordenada mas cerca del punto de solicitud
                Coordinate coordenadaMasCercana = null;
                Double distanciaMinima = Double.MAX_VALUE;
                for(Coordinate coordenada: coordenadas){
                    double distancia = coordenada.distance(puntoSolicitud.getCoordinate());
                    if(distancia < distanciaMinima){
                        distanciaMinima = distancia;
                        coordenadaMasCercana = coordenada;
                    }
                }
                coordenadaLevante = coordenadaMasCercana;
            }

            //Se envian los datos del auto junto a la coordenada de levante
            DtCoordenada puntoLevante = new DtCoordenada(coordenadaLevante.getX(), coordenadaLevante.getY());
            DtAutoExtendido autoYpuntoLevante = new DtAutoExtendido(autoConverter.toDto(autoMasCercano), puntoLevante);
            return ResponseEntity.ok(autoYpuntoLevante);
        }
        //return ResponseEntity.ok(autoConverter.toDto(autoMasCercano));
    }

    public ResponseEntity<?> AutosYSucursalesCercanos(String ptoSolicitud) throws Exception{
        //Instancio WKTReader para leer el ptoSolicitud y convertirlo a geometria
        WKTReader wktReader = new WKTReader();
        Geometry puntoSolicitud = wktReader.read(ptoSolicitud);

        //Obtengo todos los autos y sucursales
        List<Auto> autos = autoRepo.findAll();
        List<Sucursal> sucursales = sucursalRepository.findAll();

        //Actualizo lista de autos con los unicos que contienen al ptoSolicitud en su zona de cobertura
        autos = autos.stream()
                     .filter(auto -> puntosEnBuffer(auto, puntoSolicitud))
                     .collect(Collectors.toList());

        if(autos.isEmpty()){
            throw new Exception("No hay autos cercanos a tu ubicacion");
        }

        //Conseguimos la union de las zonas de cobertura de los autos y actualizamos lista de sucursales con las unicas que estan dentro de esa union.
        List<Geometry> listaBuffers = new ArrayList<Geometry>();

        for(Auto auto: autos){
            Geometry zonaCobertura = metodosGeo.calcularBuffer(auto.getRecorrido(), auto.getDist_max());

            if (!zonaCobertura.isValid()) {
                zonaCobertura = zonaCobertura.buffer(0);
            }

            listaBuffers.add(zonaCobertura);
        }

        Geometry unionZonaCoberturas = GeometryCombiner.combine(listaBuffers);

        if (!unionZonaCoberturas.isValid()) {
            unionZonaCoberturas = unionZonaCoberturas.buffer(0);
        }
        Geometry unionZonaCoberturasReparado = unionZonaCoberturas; //Para evitar un error
        sucursales = sucursales.stream()
                               .filter(sucursal -> metodosGeo.estaDentroDeBuffer(sucursal.getUbicacion(), unionZonaCoberturasReparado))
                               .collect(Collectors.toList());

        //Convierto autos a DtAuto
        List<DtAuto> dtAutos = autos.stream()
                                    .map(auto -> autoConverter.toDto(auto))
                                    .collect(Collectors.toList());

        List<DtSucursal> dtSucursales = sucursales.stream()
                                                  .map(sucursal -> sucursalConverter.toDt(sucursal))
                                                  .collect(Collectors.toList());
        
        DtListAutoSucursal dtListAutoSucursal = new DtListAutoSucursal(dtAutos, dtSucursales);
        return ResponseEntity.ok(dtListAutoSucursal);
    }
}
