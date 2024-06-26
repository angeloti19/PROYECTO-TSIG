package com.tsig.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tsig.backend.datatypes.DtAuto;
import com.tsig.backend.datatypes.DtCreacionAuto;
import com.tsig.backend.exceptions.AutoException;
import com.tsig.backend.services.AutoService;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/automotora/{atmId}/auto")
public class AutoController {

    @Autowired
    private AutoService autoService;

    @GetMapping(path = "/{autId}")
    public ResponseEntity<?> obtenerDtAutoPorId(@PathVariable("atmId") Long atmId, @PathVariable("autId") String autId) throws AutoException, Exception{
        try {
            return ResponseEntity.ok(autoService.obtenerDtAutoPorId(atmId, autId));
        }catch (AutoException a) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(a.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarAutos(@PathVariable("atmId") Long atmId) throws AutoException, Exception{
        
        try {
            return ResponseEntity.ok(autoService.listarAutos(atmId));
        }catch (AutoException a) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(a.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crearAuto(@PathVariable("atmId") Long atmId, @RequestBody DtCreacionAuto dtCreacionAuto) throws AutoException, Exception{
        try {
            DtAuto dtAuto = new DtAuto(dtCreacionAuto.getMatricula(), dtCreacionAuto.getDist_max(), dtCreacionAuto.getElectrico(), atmId, dtCreacionAuto.getRecorrido());
            return autoService.crearAuto(dtAuto);
        }catch (AutoException a) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(a.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(path = "/{autId}")
    public ResponseEntity<?> editarRecorrido(@PathVariable("atmId") Long atmId, @PathVariable("autId") String autId, @RequestBody DtCreacionAuto dtCreacionAuto) throws AutoException, Exception{
        try {
            DtAuto dtAuto = new DtAuto(dtCreacionAuto.getMatricula(), dtCreacionAuto.getDist_max(), dtCreacionAuto.getElectrico(), atmId, dtCreacionAuto.getRecorrido());
            return ResponseEntity.ok(autoService.editarRecorrido(dtAuto));
        }catch (AutoException a) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(a.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/{autId}")
    public ResponseEntity<?> eliminarAuto(@PathVariable("atmId") Long atmId, @PathVariable("autId") String autId) throws AutoException, Exception{
        try {
            return ResponseEntity.ok(autoService.eliminarAuto(atmId, autId));
        }catch (AutoException a) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(a.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
