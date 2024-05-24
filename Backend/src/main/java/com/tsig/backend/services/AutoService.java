package com.tsig.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.operation.buffer.BufferParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tsig.backend.entities.Auto;
import com.tsig.backend.entities.Automotora;
import com.tsig.backend.entities.Sucursal;
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

       public Geometry calcularBuffer(LineString recorrido, Double dist_max ){
        // Crear un objeto BufferParameters para configurar el estilo de la tapa final del buffer
        BufferParameters bufferParameters = new BufferParameters();
        // Establecer el estilo de la tapa final del buffer (redondeada)
        bufferParameters.setEndCapStyle(BufferParameters.CAP_ROUND);
        
        // Crear un objeto BufferOp con el recorrido y los parámetros del buffer
        BufferOp bufferOp = new BufferOp(recorrido, bufferParameters);
        
        // Obtener y devolver la geometría resultante del buffer con la distancia máxima especificada
        return bufferOp.getResultGeometry(dist_max);

    }

    public DtAuto obtenerDtAutoPorId(Long atmId, String autId) throws AutoException{
        Optional<Automotora> automotoraOpt = automotoraRepository.findById(atmId);
        if(!automotoraOpt.isPresent()){
            throw new AutoException("La automotora ingresada no existe");
        }

        Automotora automotora = automotoraOpt.get();
        boolean autoEncontrado = false;
        DtAuto dtAuto = new DtAuto();

        for(Auto auto : automotora.getAutos()){
            if(auto.getMatricula().equals(autId)){
                autoEncontrado = true;
                dtAuto = autoConverter.toDto(auto);
            }
            break;
        }

        if(!autoEncontrado){
            throw new AutoException("El auto ingresado no pertenece a la automotora o no existe");
        }

        return dtAuto;
    }

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

    public ResponseEntity<?> editarRecorrido(DtAuto dtAuto) throws AutoException {

        Optional<Automotora> automotoraOpt = automotoraRepository.findById(dtAuto.getIdAutomotora()); //Busco a la automotora pasada por parametro
        if(!automotoraOpt.isPresent()){
            throw new AutoException("La automotora ingresada no existe");
        }

        Automotora automotora = automotoraOpt.get(); //Obtengo a la entidad de autmotora
        boolean autoEncontrado = false;

        List<Sucursal> sucursales = automotora.getSucursales(); //Obtengo lista de sucursales de la automotora

        for(Auto auto: automotora.getAutos()){ //Recorremos lista de autos de la automotora
            if(auto.getMatricula().equals(dtAuto.getMatricula())){ //Validamos si el auto pasada por parametro existe
                autoEncontrado = true;

                GeometryFactory Factory = new GeometryFactory(new PrecisionModel(), 32721);
                Coordinate[] coordinate = dtAuto.getRecorrido().stream() //Convierto la lista de DtCoordenadas en un arreglo de Coordinate para crear la LINEA
                                                            .map(coord -> new Coordinate(coord.getX(), coord.getY()))
                                                            .toArray(Coordinate[]::new);
                LineString recorridoAutoEditado =  Factory.createLineString(coordinate);

                DtCoordenada primerCoordenada = dtAuto.getRecorrido().get(0); //Obtengo la primer coordenada del recorrido
                Coordinate coordenadaUbiAuto = new Coordinate(primerCoordenada.getX(), primerCoordenada.getY()); // Creo las coordenadas para el pto de ubicacion del auto
                Point ubiAutoEditado = Factory.createPoint(coordenadaUbiAuto); //Se crea el punto de ubicacion del auto

                Geometry zonaCobertura = this.calcularBuffer(recorridoAutoEditado, dtAuto.getDist_max());

                boolean haySucursalEnCobertura = sucursales.stream().anyMatch(sucursal-> zonaCobertura.contains(sucursal.getUbicacion()));
                
                if(!haySucursalEnCobertura){
                    throw new AutoException("EL RECORRIDO NUEVO NO TIENE A NIGUNA SUCURSAL DENTRO DE SU ZONA DE COBERTURA");
                }
                
                auto.setMatricula(dtAuto.getMatricula());
                auto.setDist_max(dtAuto.getDist_max());
                auto.setRecorrido(recorridoAutoEditado);
                auto.setUbicacion(ubiAutoEditado);
            }
            break;
        }

        if(!autoEncontrado){
            throw new AutoException("El auto no pertenece a la automotora ingresda o no existe");
        }

        automotoraRepository.save(automotora);

        return ResponseEntity.ok("Auto modificado correctamente!!");
    }

    public ResponseEntity<?> crearAuto(DtAuto dtAuto) throws AutoException {

        Optional<Automotora> automotoraOpt = automotoraRepository.findById(dtAuto.getIdAutomotora());
        if(!automotoraOpt.isPresent()){
            throw new AutoException("La automotora ingresada no existe");
        }

        Automotora automotora = automotoraOpt.get(); //Obtengo a la entidad de autmotora

        List<Sucursal> sucursales = automotora.getSucursales(); //Obtengo lista de sucursales de la automotora

        GeometryFactory Factory = new GeometryFactory(new PrecisionModel(), 32721);
        Coordinate[] coordinate = dtAuto.getRecorrido().stream() //Convierto la lista de DtCoordenadas en un arreglo de Coordinate para crear la LINEA
                                                       .map(coord -> new Coordinate(coord.getX(), coord.getY()))
                                                       .toArray(Coordinate[]::new);
        LineString recorridoAuto =  Factory.createLineString(coordinate);

        
        DtCoordenada primerCoordenada = dtAuto.getRecorrido().get(0); //Obtengo la primer coordenada del recorrido
        Coordinate coordenadaUbiAuto = new Coordinate(primerCoordenada.getX(), primerCoordenada.getY()); // Creo las coordenadas para el pto de ubicacion del auto
        Point ubiAuto = Factory.createPoint(coordenadaUbiAuto); //Se crea el punto de ubicacion del auto
       
        Geometry zonaCobertura = this.calcularBuffer(recorridoAuto, dtAuto.getDist_max());

        boolean haySucursalEnCobertura = sucursales.stream().anyMatch(sucursal-> zonaCobertura.contains(sucursal.getUbicacion()));

        if(!haySucursalEnCobertura){
            throw new AutoException("EL RECORRIDO NO TIENE A NIGUNA SUCURSAL DENTRO DE SU ZONA DE COBERTURA");
        }

        Auto auto = new Auto(dtAuto.getMatricula(), dtAuto.getDist_max(), recorridoAuto, dtAuto.getElectrico(), ubiAuto, automotora);

        automotora.agregarAuto(auto);
        automotoraRepository.save(automotora);

        return ResponseEntity.ok("Auto creado correctamente!!");

    }


}
