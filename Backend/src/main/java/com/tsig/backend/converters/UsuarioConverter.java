package com.tsig.backend.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tsig.backend.dto.UsuarioDto;
import com.tsig.backend.models.UsuarioModel;

@Component
public class UsuarioConverter {
    
    @Autowired
    public UsuarioConverter(){}

    public UsuarioModel toModel(UsuarioDto userDto){
        UsuarioModel usuario = new UsuarioModel();

        usuario.setId(userDto.getId());
        usuario.setNombre(userDto.getNombre());
        usuario.setUsuario(userDto.getUsuario());
        usuario.setCorreo(userDto.getCorreo());
        usuario.setContrasenia(userDto.getContrasenia());
        return usuario;
    }

    public UsuarioDto toDto(UsuarioModel usuario){
        UsuarioDto usuarioDto = new UsuarioDto();

        usuarioDto.setId(usuario.getId());
        usuarioDto.setNombre(usuario.getNombre());
        usuarioDto.setUsuario(usuario.getUsuario());
        usuarioDto.setCorreo(usuario.getCorreo());
        usuarioDto.setContrasenia(usuario.getContrasenia());

        return usuarioDto;

    }

}
