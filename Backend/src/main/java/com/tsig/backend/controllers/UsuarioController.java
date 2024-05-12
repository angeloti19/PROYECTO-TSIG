package com.tsig.backend.controllers;



import java.security.KeyStore.PasswordProtection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsig.backend.exceptions.UsuarioException;
import com.tsig.backend.models.UsuarioModel;
import com.tsig.backend.services.UsuarioService;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService userService;

    PasswordProtection pass;

    @GetMapping("/listar")
    public ResponseEntity<?> obtenerUsuarios(){
        try {
            return ResponseEntity.ok(userService.obtenerUsuarios()); 
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        
    }
    
    @PostMapping
    public ResponseEntity<?> guardarUsuario(@RequestBody UsuarioModel usuario) throws UsuarioException, Exception{
        
        try {
            return ResponseEntity.ok(userService.guardarUsuario(usuario));

        } catch (UsuarioException u) {
            return new ResponseEntity(u.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable("id") Long id) throws UsuarioException, Exception{
        
        try {
            return ResponseEntity.ok(this.userService.obtenerUserPorId(id));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }   
    }

}