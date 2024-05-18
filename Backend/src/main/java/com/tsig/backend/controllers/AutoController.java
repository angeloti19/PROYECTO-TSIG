package com.tsig.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tsig.backend.datatypes.DtAuto;
import com.tsig.backend.entities.Auto;
import com.tsig.backend.exceptions.AutoException;
import com.tsig.backend.services.AutoService;

@RestController
@RequestMapping("/api/autos")
public class AutoController {

    @Autowired
    private AutoService autoService;

    @PostMapping
    public ResponseEntity<Auto> guardarAuto(@RequestBody DtAuto autoDto) {
        try {
            Auto savedAuto = autoService.guardarAuto(autoDto);
            return ResponseEntity.ok(savedAuto);
        } catch (AutoException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
