package com.example.libraryapi.controller.mappers;

import com.example.libraryapi.controller.dto.CadastroLivroDTO;
import com.example.libraryapi.model.Livro;
import com.example.libraryapi.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class LivroMapper {

    @Autowired
    AutorRepository autorRepository;

    @Mapping(target = "autor", expression = "java( autorRepository.findById(livroDTO.idAutor()).orElse(null) )")
    public abstract Livro toEntity(CadastroLivroDTO livroDTO);

}
