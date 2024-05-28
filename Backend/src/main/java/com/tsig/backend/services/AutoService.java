package com.tsig.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tsig.backend.entities.Auto;
import com.tsig.backend.entities.Automotora;
import com.tsig.backend.entities.Sucursal;
import com.tsig.backend.exceptions.AutoException;
import com.tsig.backend.repositories.AutoRepository;
import com.tsig.backend.repositories.AutomotoraRepository;
import com.tsig.backend.utils.MetodosGeo;
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

    MetodosGeo metodosGeo = new MetodosGeo();

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

    public ResponseEntity<?> crearAuto(DtAuto dtAuto) throws AutoException {

        Optional<Automotora> automotoraOpt = automotoraRepository.findById(dtAuto.getIdAutomotora());
        if(!automotoraOpt.isPresent()){
            throw new AutoException("La automotora ingresada no existe");
        }

        Automotora automotora = automotoraOpt.get(); //Obtengo a la entidad de autmotora

        List<Sucursal> sucursales = automotora.getSucursales(); //Obtengo lista de sucursales de la automotora
         
        LineString recorridoAuto =  metodosGeo.crearRecorridoAuto(dtAuto); //Creo recorrido de auto tipo LineString

        Point ubiAuto = metodosGeo.crearUbicacionAuto(dtAuto);  // Se crea ubicacion de auto 
        
        Geometry zonaCobertura = metodosGeo.calcularBuffer(recorridoAuto, dtAuto.getDist_max()); //Obtenemos la zona de cobertura de cada auto

        //Recorremos las sucursales y validamos que dentro de la zona de cobertura se contenga la ubicacion de al menos una sucursañ
        boolean haySucursalEnCobertura = sucursales.stream().anyMatch(sucursal-> zonaCobertura.contains(sucursal.getUbicacion())); 

        // Si no hay ninguna, lanzamos excepcion 
        if(!haySucursalEnCobertura){
            throw new AutoException("EL RECORRIDO NO TIENE A NIGUNA SUCURSAL DENTRO DE SU ZONA DE COBERTURA");
        }

        //En caso contrario, procedemos a crear el auto pasandole sus atributos por constructor.
        Auto auto = new Auto(dtAuto.getMatricula(), dtAuto.getDist_max(), recorridoAuto, dtAuto.getElectrico(), ubiAuto, automotora);

        //Agregamos el auto a la lista de autos que tiene la automotora
        automotora.agregarAuto(auto);

        //Guardamos
        automotoraRepository.save(automotora);

        return ResponseEntity.ok("Auto creado correctamente!!");

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
            if(auto.getMatricula().equals(dtAuto.getMatricula())){ //Validamos si el auto pasado por parametro existe
                autoEncontrado = true;

                
                LineString recorridoAutoEditado =  metodosGeo.crearRecorridoAuto(dtAuto); // Creamos recorrido auto

                Point ubiAutoEditado = metodosGeo.crearUbicacionAuto(dtAuto); //Creamos ubicacion auto

                Geometry zonaCobertura = metodosGeo.calcularBuffer(recorridoAutoEditado, dtAuto.getDist_max()); // Calculamos zona de cobertura

                //Validamos que haya al menos una sucursal dentro de la zona de cobertura
                boolean haySucursalEnCobertura = sucursales.stream().anyMatch(sucursal-> zonaCobertura.contains(sucursal.getUbicacion()));
                
                if(!haySucursalEnCobertura){
                    throw new AutoException("EL RECORRIDO NUEVO NO TIENE A NIGUNA SUCURSAL DENTRO DE SU ZONA DE COBERTURA");
                }
                
                //Si hay al menos una sucursal, seteamos los nuevos datos del auto.
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

    public ResponseEntity<?> eliminarAuto(Long atmId, String autId) throws AutoException{

        Automotora automotora = automotoraRepository.findById(atmId).orElseThrow(() ->
                    new AutoException("La automotora no existe."));

        Auto autoAEliminar = automotora.getAutos().stream()
                                                  .filter(auto -> auto.getMatricula().equals(autId))
                                                  .findFirst()
                                                  . orElseThrow(()-> new AutoException("El auto no pertenece a la automotora ingresada"));

        automotora.getAutos().remove(autoAEliminar);
        autoRepo.delete(autoAEliminar);

        automotoraRepository.save(automotora);

        return ResponseEntity.ok("Auto eliminado con exito!");
    }


}
