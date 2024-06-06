package com.tsig.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tsig.backend.datatypes.DtSolicitudAuto;
import com.tsig.backend.exceptions.AutoException;
import com.tsig.backend.services.SolicitudAutoService;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/autoSolicitud")
public class SolicitarAutoController {

    @Autowired
    SolicitudAutoService solicitudAutoService;

    @GetMapping(path = "/{ptoSol}/{ptoDest}")
    public ResponseEntity<?> solicitarAuto(@PathVariable("ptoSol") String ptoSol,
                                           @PathVariable("ptoDest") String ptoDest,
                                           @RequestParam(required = false) String electrico, 
                                           @RequestParam(required = false) Long idAutomotora, 
                                           @RequestParam(required = false) Float dist_max) throws AutoException, Exception{
      try{
        DtSolicitudAuto dtSolicitudAuto = new DtSolicitudAuto(ptoSol, ptoDest, idAutomotora, electrico, dist_max);
        return ResponseEntity.ok(solicitudAutoService.solicitarAuto(dtSolicitudAuto));
      }catch (AutoException a) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(a.getMessage());
      }catch (Exception e) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
      }
    }

}
    
    



