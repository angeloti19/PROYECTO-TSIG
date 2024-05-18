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
        Usuario usuario = new Usuario();

        usuario.setId(userDto.getId());
        usuario.setNombre(userDto.getNombre());
        usuario.setCorreo(userDto.getCorreo());
        usuario.setContrasenia(userDto.getContrasenia());
        return usuario;
    }

    public DtUsuario toDto(Usuario usuario){
        DtUsuario usuarioDto = new DtUsuario();

        usuarioDto.setId(usuario.getId());
        usuarioDto.setNombre(usuario.getNombre());
        usuarioDto.setCorreo(usuario.getCorreo());
        usuarioDto.setContrasenia(usuario.getContrasenia());

        return usuarioDto;

    }

}
