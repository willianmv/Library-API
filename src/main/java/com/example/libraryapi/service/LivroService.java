package com.example.libraryapi.service;

import com.example.libraryapi.model.GeneroLivro;
import com.example.libraryapi.model.Livro;
import com.example.libraryapi.repository.LivroRepository;
import com.example.libraryapi.repository.specs.LivroSpecs;
import com.example.libraryapi.security.SecurityService;
import com.example.libraryapi.validator.LivroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final LivroValidator livroValidator;
    private final SecurityService securityService;


    public Livro salvar(Livro livro) {
        livroValidator.validarLivro(livro);
        livro.setUsuario(securityService.obterUsuarioLogado());
        return livroRepository.save(livro);
    }

    public Optional<Livro> obterPorId(UUID id){
        return livroRepository.findById(id);
    }

    public void delete(Livro livro){
        livroRepository.delete(livro);
    }

    public Page<Livro> pesquisa
            (String isbn,
             String titulo,
             String nomeAutor,
             GeneroLivro genero,
             Integer anoPublicacao,
             Integer pagina,
             Integer tamanhoPagina){
        Specification<Livro> specs = Specification.where(((root, query, cb) -> cb.conjunction() ));

        if(isbn != null){
            specs = specs.and(LivroSpecs.isbnEqual(isbn));
        }

        if(titulo != null){
            specs = specs.and(LivroSpecs.tituloLike(titulo));
        }

        if(genero != null){
            specs = specs.and(LivroSpecs.generoEqual(genero));
        }

        if(anoPublicacao != null){
            specs = specs.and(LivroSpecs.anoPublicacaoEqual(anoPublicacao));
        }

        if(nomeAutor != null){
            specs = specs.and(LivroSpecs.nomeAutorLike(nomeAutor));
        }

        Pageable pageRequest = PageRequest.of(pagina, tamanhoPagina);

        return livroRepository.findAll(specs, pageRequest);
    }

    public void atualizar(Livro livro) {
        if(livro.getId() == null){
            throw new IllegalArgumentException("Para atualizar é necessário que o livro esteja cadastrado");
        }
        livroValidator.validarLivro(livro);
        livroRepository.save(livro);
    }
}
