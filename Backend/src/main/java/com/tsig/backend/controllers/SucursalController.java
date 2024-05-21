package com.tsig.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsig.backend.datatypes.DtCreacionSucursal;
import com.tsig.backend.datatypes.DtSucursal;
import com.tsig.backend.exceptions.SucursalException;
import com.tsig.backend.services.SucursalService;

@RestController
@RequestMapping("/api/automotora/{atmId}/sucursal")
public class SucursalController {

    @Autowired
    SucursalService sucursalService;

    @GetMapping
    public ResponseEntity<?> listarSucursal (@PathVariable("atmId") Long atmId) throws SucursalException, Exception{
        try {
            return ResponseEntity.ok(sucursalService.listarSucursales(atmId));
        }catch (SucursalException s) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(s.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PostMapping
    public ResponseEntity<?> crearSucursal(@PathVariable("atmId") Long atmId, @RequestBody  DtCreacionSucursal dtCreacionSucursal) throws SucursalException, Exception{
        try {
            DtSucursal dtSucursal = new DtSucursal(null, dtCreacionSucursal.getNombre(), dtCreacionSucursal.getCoordenadas(), atmId); 
            return sucursalService.crearSucursal(dtSucursal);
        }catch (SucursalException s) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(s.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(path = "/{sucId}")
    public ResponseEntity<?> actualizarSucursal(@PathVariable("atmId") Long atmId, @PathVariable("sucId") Long sucId, @RequestBody  DtCreacionSucursal dtCreacionSucursal) throws SucursalException, Exception{

        try {
            DtSucursal dtSucursal = new DtSucursal(sucId, dtCreacionSucursal.getNombre(), dtCreacionSucursal.getCoordenadas(), atmId); 
            return sucursalService.modificarSucursal(dtSucursal);
        }catch (SucursalException s) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(s.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
