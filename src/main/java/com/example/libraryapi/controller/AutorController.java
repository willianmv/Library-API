package com.example.libraryapi.controller;

import com.example.libraryapi.controller.dto.AutorDTO;
import com.example.libraryapi.controller.mappers.AutorMapper;
import com.example.libraryapi.model.Autor;
import com.example.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController implements GenericController {

    private final AutorService autorService;
    private final AutorMapper autorMapper;

    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO autorDTO) {
        Autor autorEntidade = autorMapper.toEntity(autorDTO);
        autorService.salvar(autorEntidade);
        URI location = gerarHeaderLocation(autorEntidade.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id) {
        UUID autorId = UUID.fromString(id);
        return autorService.obterPorId(autorId)
                .map(autor -> {
                    AutorDTO dto = autorMapper.toDTO(autor);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> deletar(@PathVariable("id") String id) {
        UUID autorId = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorId(autorId);
        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        autorService.deletar(autorOptional.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<List<AutorDTO>> pesquisar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade) {
                List<Autor> autoresPesquisados = autorService.pesquisaByExample(nome, nacionalidade);
                List<AutorDTO> list = autoresPesquisados
                    .stream()
                    .map(autorMapper::toDTO)
                    .toList();
                    return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> atualizar(@PathVariable String id, @RequestBody @Valid AutorDTO autorDTO) {
        UUID autorId = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorId(autorId);
        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var autor = autorOptional.get();
        autor.setNome(autorDTO.nome());
        autor.setNacionalidade(autorDTO.nacionalidade());
        autor.setDataNascimento(autorDTO.dataNascimento());
        autorService.atualizar(autor);
        return ResponseEntity.noContent().build();
    }

}
