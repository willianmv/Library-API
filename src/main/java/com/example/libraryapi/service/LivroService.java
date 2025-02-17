package com.example.libraryapi.service;

import com.example.libraryapi.model.Livro;
import com.example.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;


    public Livro salvar(Livro livro) {
        return livroRepository.save(livro);
    }
}
