package com.example.libraryapi.controller;

import com.example.libraryapi.controller.dto.CadastroLivroDTO;
import com.example.libraryapi.controller.dto.ErroResposta;
import com.example.libraryapi.controller.mappers.LivroMapper;
import com.example.libraryapi.exception.RegistroDuplicadoException;
import com.example.libraryapi.model.Livro;
import com.example.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController implements GenericController{

    private final LivroService livroService;
    private final LivroMapper livroMapper;

    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody @Valid CadastroLivroDTO cadastroLivroDTO){
        Livro livro = livroMapper.toEntity(cadastroLivroDTO);
        Livro livroSalvo = livroService.salvar(livro);
        URI location = gerarHeaderLocation(livroSalvo.getId());
        return ResponseEntity.created(location).build();
    }
}
