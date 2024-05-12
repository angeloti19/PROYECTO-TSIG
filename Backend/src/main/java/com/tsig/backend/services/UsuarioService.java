package com.tsig.backend.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsig.backend.dto.UsuarioDto;
import com.tsig.backend.exceptions.UsuarioException;
import com.tsig.backend.models.UsuarioModel;
import com.tsig.backend.repositories.UsuarioRepository;
import com.tsig.backend.converters.UsuarioConverter;

@Service
public class UsuarioService {
    
    @Autowired
    UsuarioRepository usuarioRepo;
    UsuarioConverter usuarioConverter;


    public ArrayList<UsuarioModel> obtenerUsuarios(){
        return (ArrayList<UsuarioModel>)usuarioRepo.findAll();
    };

    public UsuarioModel guardarUsuario(UsuarioModel usuario) throws UsuarioException{
        return usuarioRepo.save(usuario);
    }

    public Optional<UsuarioModel> obtenerUserPorId (Long id){
        return usuarioRepo.findById(id);
    }

    public UsuarioDto obtenerUsuarioDto(String correo, String usuario){
        UsuarioModel user = usuarioRepo.findByCorreoOrUsuario(correo, usuario);
        UsuarioDto userDto = usuarioConverter.toDto(user);
        return userDto;

    }



}
