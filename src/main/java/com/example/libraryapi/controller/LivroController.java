package com.example.libraryapi.controller;

import com.example.libraryapi.controller.dto.CadastroLivroDTO;
import com.example.libraryapi.controller.dto.PesquisaLivroDTO;
import com.example.libraryapi.controller.mappers.LivroMapper;
import com.example.libraryapi.model.GeneroLivro;
import com.example.libraryapi.model.Livro;
import com.example.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController implements GenericController{

    private final LivroService livroService;
    private final LivroMapper livroMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Void> salvar(@RequestBody @Valid CadastroLivroDTO cadastroLivroDTO){
        Livro livro = livroMapper.toEntity(cadastroLivroDTO);
        Livro livroSalvo = livroService.salvar(livro);
        URI location = gerarHeaderLocation(livroSalvo.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<PesquisaLivroDTO> obterDetalhes(@PathVariable("id") String id){
        return livroService.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    var livroDTO = livroMapper.toDTO(livro);
                    return ResponseEntity.ok(livroDTO);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Object> delete(@PathVariable("id") String id){
        return livroService.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    livroService.delete(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Page<PesquisaLivroDTO>> lista (
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "titulo", required = false)
            String titulo,
            @RequestParam(value = "nome-autor", required = false)
            String nomeAutor,
            @RequestParam(value = "genero", required = false)
            GeneroLivro genero,
            @RequestParam(value = "ano-publicacao", required = false)
            Integer anoPublicacao,
            @RequestParam(value = "pagina", defaultValue = "0")
            Integer pagina,
            @RequestParam(value = "tamanho-pagina", defaultValue = "10")
            Integer tamanhoPagina
            ){

        var paginaResultado = livroService.pesquisa(isbn, titulo, nomeAutor, genero, anoPublicacao, pagina, tamanhoPagina);
        Page<PesquisaLivroDTO> resutado = paginaResultado.map(livroMapper::toDTO);
        return ResponseEntity.ok(resutado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Object> atualizar(
            @PathVariable("id") String id,
            @RequestBody @Valid CadastroLivroDTO livroDTO){
            return livroService.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    Livro entidadeAux = livroMapper.toEntity(livroDTO);

                    livro.setDataPublicacao(entidadeAux.getDataPublicacao());
                    livro.setIsbn(entidadeAux.getIsbn());
                    livro.setPreco(entidadeAux.getPreco());
                    livro.setGenero(entidadeAux.getGenero());
                    livro.setTitulo(entidadeAux.getTitulo());
                    livro.setAutor(entidadeAux.getAutor());

                    livroService.atualizar(livro);

                    return ResponseEntity.noContent().build();

                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
