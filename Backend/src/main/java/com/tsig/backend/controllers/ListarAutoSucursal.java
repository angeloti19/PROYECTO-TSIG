package com.tsig.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tsig.backend.services.SolicitudAutoService;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/autoSucursalCercanos")
public class ListarAutoSucursal {
    
    @Autowired
    SolicitudAutoService solicitudAutoService;

    @GetMapping(path = "/{ptoSol}")
    public ResponseEntity<?> autosYSucursalesCercanos(@PathVariable("ptoSol") String ptoSol) throws Exception{
        try {
            return ResponseEntity.ok(solicitudAutoService.AutosYSucursalesCercanos(ptoSol));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage() );
        }
    }
}
