package com.example.libraryapi.controller;

import com.example.libraryapi.controller.dto.AutorDTO;
import com.example.libraryapi.controller.mappers.AutorMapper;
import com.example.libraryapi.model.Autor;
import com.example.libraryapi.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Tag(name = "Autores")
@Slf4j
public class AutorController implements GenericController {

    private final AutorService autorService;
    private final AutorMapper autorMapper;

    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Salvar", description = "Cadastrar novo autor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro de validação."),
            @ApiResponse(responseCode = "409", description = "Autor já está cadastrado.")
    })
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO autorDTO) {
        log.info("Cadastrando novo autor {}", autorDTO.nome());

        Autor autorEntidade = autorMapper.toEntity(autorDTO);
        autorService.salvar(autorEntidade);
        URI location = gerarHeaderLocation(autorEntidade.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Obter detalhes", description = "Obter dados do autor por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor encontrado"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado.")
    })
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
    @Operation(summary = "Deletar", description = "Deletar autor existente por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado."),
            @ApiResponse(responseCode = "400", description = "Autor possui livro cadastrado.")
    })
    public ResponseEntity<Void> deletar(@PathVariable("id") String id) {

        log.info("Deletando autor de ID: {}", id);

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
    @Operation(summary = "Pesquisar", description = "Realizar pesquisa de autores por parâmetros.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sucesso.")

    })
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
    @Operation(summary = "Atualizar", description = "Atualizar autor existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado."),
            @ApiResponse(responseCode = "409", description = "Autor já cadastrado.")
    })
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
