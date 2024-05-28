package com.tsig.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tsig.backend.services.ConsultasGeoService;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/consultas")
public class ConsultasGeoController {

    @Autowired
    ConsultasGeoService consultasGeoService;

    @GetMapping("/zonasSinCobertura")
    public ResponseEntity<?> zonasSinCobertura() throws Exception{
        try {
            return ResponseEntity.ok(consultasGeoService.zonasSinCobertura());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
}
