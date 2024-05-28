package com.tsig.backend.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsig.backend.entities.Usuario;
import com.tsig.backend.exceptions.UsuarioException;
import com.tsig.backend.repositories.UsuarioRepository;
import com.tsig.backend.converters.UsuarioConverter;
import com.tsig.backend.datatypes.DtUsuario;

@Service
public class UsuarioService {
    
    @Autowired
    UsuarioRepository usuarioRepo;
    UsuarioConverter usuarioConverter;

    public ArrayList<Usuario> obtenerUsuarios(){
        return (ArrayList<Usuario>)usuarioRepo.findAll();
    }

    public Usuario guardarUsuario(Usuario usuario) throws UsuarioException{
        return usuarioRepo.save(usuario);
    }

    public Optional<Usuario> obtenerUserPorId (Long id){
        return usuarioRepo.findById(id);
    }

    public DtUsuario obtenerUsuarioDto(String correo){
        Usuario user = usuarioRepo.findByCorreo(correo);
        DtUsuario userDto = usuarioConverter.toDto(user);
        return userDto;
    }

}
