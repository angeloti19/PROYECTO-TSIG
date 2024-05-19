package com.tsig.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsig.backend.datatypes.DtCreacionAutomotora;
import com.tsig.backend.exceptions.AutomotoraException;
import com.tsig.backend.services.AutomotoraService;

@RestController
@RequestMapping("/api/automotora")
public class AutomotoraController {
    
    @Autowired
    AutomotoraService automotoraService;

    @GetMapping
    public ResponseEntity<?> listarAutomotoras() throws AutomotoraException, Exception{
        try {
            return ResponseEntity.ok(automotoraService.obtenerDtAutomotoras());
        }catch (AutomotoraException a) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(a.getMessage());
        }catch (Exception e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crearAutomotora(@RequestBody DtCreacionAutomotora dtCreacionAutomotora) throws AutomotoraException, Exception{
        try {
            return automotoraService.crearAutomotora(dtCreacionAutomotora);
        }catch (AutomotoraException a) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(a.getMessage());
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
