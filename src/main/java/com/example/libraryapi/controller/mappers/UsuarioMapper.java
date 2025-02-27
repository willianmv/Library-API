package com.example.libraryapi.controller.mappers;

import com.example.libraryapi.controller.dto.UsuarioDTO;
import com.example.libraryapi.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO usuarioDTO);

}
