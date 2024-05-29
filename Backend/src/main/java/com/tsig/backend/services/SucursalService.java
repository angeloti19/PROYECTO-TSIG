package com.tsig.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tsig.backend.converters.SucursalConverter;
import com.tsig.backend.datatypes.DtSucursal;
import com.tsig.backend.entities.Auto;
import com.tsig.backend.entities.Automotora;
import com.tsig.backend.entities.Sucursal;
import com.tsig.backend.exceptions.SucursalException;
import com.tsig.backend.repositories.AutomotoraRepository;
import com.tsig.backend.repositories.SucursalRepository;
import com.tsig.backend.utils.MetodosGeo;

import jakarta.transaction.Transactional;

@Service
public class SucursalService {
    
    @Autowired
    SucursalRepository sucursalRepository;

    @Autowired
    AutomotoraRepository automotoraRepository;

    @Autowired
    SucursalConverter sucursalConverter;

    MetodosGeo metodosGeo = new MetodosGeo(); //Instancio clase de utils MetodosGeo 

    //OBTENER SUCURSAL POR ID
    public DtSucursal obteDtSucursalPorId(Long atmId, Long sucId) throws SucursalException{

        Optional<Automotora> automotoraOpt = automotoraRepository.findById(atmId);
        if(!automotoraOpt.isPresent()){
            throw new SucursalException("La automotora ingresada no existe");
        }

        Automotora automotoraEntidad = automotoraOpt.get();
        boolean sucursalEncontrada = false;
        DtSucursal dtSucursal = new DtSucursal();

        for(Sucursal sucursal : automotoraEntidad.getSucursales()){
            if (sucursal.getId().equals(sucId)) {
                sucursalEncontrada = true;
                
                dtSucursal = sucursalConverter.toDt(sucursal);
                break;
            }
            
        }

        if (!sucursalEncontrada) {
            throw new SucursalException("La sucursal no pertenece a la automotora especificada");
        }

        return dtSucursal;
    }

    //LISTAR SUCURSALES
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

    //CREAR SUCURSAL
    public ResponseEntity<?> crearSucursal(DtSucursal dtSucursal) throws SucursalException{

        if(dtSucursal.getIdAutomotora() == null || dtSucursal.getNombre() == null || dtSucursal.getCoordenadas() == null){
            throw new SucursalException("Faltan datos para la sucursal");
        }

        Optional<Automotora> automotora = automotoraRepository.findById(dtSucursal.getIdAutomotora()); // Buscamos a la automotora por su ID
        if(!automotora.isPresent()){
            throw new SucursalException("La automotora ingresada no existe");
        }

        Point ubicacionSucursal = metodosGeo.crearUbicacionSucursal(dtSucursal);
        
        Automotora automotoraEnt = automotora.get(); //Obtenemos la automotora como entidad con la funcion .get()

        Sucursal sucursal = new Sucursal(dtSucursal.getNombre(), automotoraEnt , ubicacionSucursal); // Creamos sucursal 

        automotoraEnt.agregarSucursal(sucursal);

        automotoraRepository.save(automotoraEnt); // Actualizamos automotora con su nueva sucursal creada. 

        return ResponseEntity.ok("Sucursal "+ dtSucursal.getNombre()+" creada correctamente");
    }

    //VALIDACION
    private boolean estaDentroDeBuffer(Point ubicacion, Geometry buffer) {
        return buffer.contains(ubicacion);
    }

    //METODO VALIDACION
    public boolean puedeBorrarOActualizarSucursal(Long atmId, Long sucId, String operacion) throws SucursalException {

        Automotora automotora = automotoraRepository.findById(atmId).orElse(null); //Busco a la automotora por su ID pasada por parametro
        if (automotora == null) {
            throw new SucursalException("La automotora no existe.");
        }

        Sucursal sucursalAEliminar = automotora.getSucursales().stream() //Recorro y busco a la sucursal a eliminar por su ID pasada por parametro
                                                               .filter(sucursal -> sucursal.getId().equals(sucId))
                                                               .findFirst()
                                                               .orElse(null);
        if (sucursalAEliminar == null) {
            throw new SucursalException("La sucursal no existe en esta automotora.");
        }

        //Validamos si es la ultima sucursal de la automotora y la operacion es DELETE O PUT 
        if(automotora.getSucursales().size() == 1 && operacion == "DELETE"){
            return true;
        }else if(automotora.getSucursales().size() == 1 && operacion == "PUT"){
            return false;
        }

        //Si no es la ultima, procedemos a hacer las demas validaciones -->

        List<Auto> autos = automotora.getAutos(); //Obtengo la lista de autos de la automora ingresada
      
        for (Auto auto : autos) { //Recorro lista de autos para validar si existe otra sucursal dentro de la zona de cobertura 

            Geometry buffer = metodosGeo.calcularBuffer(auto.getRecorrido(), auto.getDist_max()); //Calculo buffer de cada auto

            // Valido que tenga otra sucursal dentro la zona de cobertura que no sea la que pasamos por parametro
            boolean tieneOtraSucursalDentroDeBuffer = automotora.getSucursales().stream() 
                    .filter(sucursal -> !sucursal.getId().equals(sucId)) //Filtro por sucursales diferentes a la que me pasaron por parametro
                    .anyMatch(sucursal -> estaDentroDeBuffer(sucursal.getUbicacion(), buffer)); //Busco si hace match alguna sucursal utilizando la funcion 'estaDentroDeBuffer' pasandole la ubicacion de la sucursal y buffer. Si hace match devuelve true

            if (!tieneOtraSucursalDentroDeBuffer) {
                return false;
            }
        }

        return true;
    }

    //MODIFICAR SUCURSAL
    public ResponseEntity<?> modificarSucursal(DtSucursal dtSucursal) throws SucursalException{

        if(dtSucursal.getIdAutomotora() == null || dtSucursal.getNombre() == null){
            throw new SucursalException("Faltan datos para la sucursal");
        }

        Optional<Automotora> automotoraOpt = automotoraRepository.findById(dtSucursal.getIdAutomotora()); // Buscamos a la automotora por su ID
        if(!automotoraOpt.isPresent()){
            throw new SucursalException("La automotora ingresada no existe");
        }

        Automotora automotoraEntidad = automotoraOpt.get();

        boolean sucursalEncontrada = false;

        for(Sucursal sucursal : automotoraEntidad.getSucursales()){
            if (sucursal.getId().equals(dtSucursal.getId())) {
                sucursalEncontrada = true;

                String operacion = "PUT";

                if (!puedeBorrarOActualizarSucursal(dtSucursal.getIdAutomotora(), dtSucursal.getId(),operacion)) {
                    throw new SucursalException("No se puede editar la sucursal porque dejaría a un auto sin cobertura.");
                }

                // Actualizar nombre si se proporciona
                if (dtSucursal.getNombre() != null) {
                    sucursal.setNombre(dtSucursal.getNombre());
                }
    
                // Actualizar coordenadas si se proporcionan
                if (dtSucursal.getCoordenadas() != null) {
                        Point ubicacionSucursal = metodosGeo.crearUbicacionSucursal(dtSucursal);
                        sucursal.setUbicacion(ubicacionSucursal);
                } 
                break;
            }
            
        }

        if (!sucursalEncontrada) {
            throw new SucursalException("La sucursal no pertenece a la automotora especificada");
        }

        automotoraRepository.save(automotoraEntidad);

        return ResponseEntity.ok("Sucursal actualizada correctamente");

    }

    //ELIMINAR SUCURSAL
    @Transactional
    public ResponseEntity<?> eliminarSucursal(Long atmId, Long sucId) throws SucursalException {

            Automotora automotora = automotoraRepository.findById(atmId).orElseThrow(() ->
                    new SucursalException("La automotora no existe."));

            Sucursal sucursalAEliminar = automotora.getSucursales().stream()
                                                                   .filter(sucursal -> sucursal.getId().equals(sucId))
                                                                   .findFirst()
                                                                   .orElseThrow(() -> new SucursalException("La sucursal no existe en esta automotora."));
            
            String operacion = "DELETE";
            if (!puedeBorrarOActualizarSucursal(atmId, sucId, operacion)) {
                throw new SucursalException("No se puede eliminar la sucursal porque dejaría a un auto sin cobertura.");
            }

            automotora.getSucursales().remove(sucursalAEliminar); //Removemos la sucursal de la lista que tiene automotora
            //automotoraRepository.save(automotora); // Guardamos la automotora actualizada

            sucursalRepository.delete(sucursalAEliminar); // Eliminamos la sucursal

            // Verificar si no quedan sucursales y eliminar autos solo si es necesario
            if (automotora.getSucursales().isEmpty()) {
                    automotora.getAutos().clear();
            }else{
                automotoraRepository.save(automotora); // Guardamos la automotora actualizada después de eliminar autos si es necesario
            }
            
            return ResponseEntity.ok("Sucursal eliminada correctamente.");
    }

}






