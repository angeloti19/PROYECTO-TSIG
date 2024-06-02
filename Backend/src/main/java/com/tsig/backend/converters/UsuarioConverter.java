package com.tsig.backend.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tsig.backend.datatypes.DtUsuario;
import com.tsig.backend.entities.Usuario;

@Component
public class UsuarioConverter {
    
    @Autowired
    public UsuarioConverter(){}

    public Usuario toModel(DtUsuario userDto){
        Usuario usuario = new Usuario(null, null, null, null, null);

        usuario.setId(userDto.getId());
        usuario.setUsername(userDto.getNombre());
        usuario.setCorreo(userDto.getCorreo());
        usuario.setPassword(userDto.getContrasenia());
        return usuario;
    }

    public DtUsuario toDto(Usuario usuario){
        DtUsuario usuarioDto = new DtUsuario();

        usuarioDto.setId(usuario.getId());
        usuarioDto.setNombre(usuario.getUsername());
        usuarioDto.setCorreo(usuario.getCorreo());
        usuarioDto.setContrasenia(usuario.getPassword());

        return usuarioDto;

    }

}
