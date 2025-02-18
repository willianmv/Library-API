package com.example.libraryapi.repository;

import com.example.libraryapi.model.Autor;
import com.example.libraryapi.model.GeneroLivro;
import com.example.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class LivroRepositoryTest {

    @Autowired
    LivroRepository livroRepository;

    @Autowired
    AutorRepository autorRepository;

    @Test
    void salvarTest(){
        Livro livro = new Livro();
        livro.setIsbn("90887-8474");
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setTitulo("UFO");
        livro.setDataPublicacao(LocalDate.of(1980,1, 2) );

        Autor autor = autorRepository
                .findById(UUID.fromString("6f12ece9-cae0-438b-bec4-05528e320d88"))
                .orElse(null);

        livro.setAutor(autor);
        livroRepository.save(livro);
    }

    @Test
    void salvarCascadeTypeTest(){
        Livro livro = new Livro();
        livro.setIsbn("94787-8754");
        livro.setGenero(GeneroLivro.MISTERIO);
        livro.setPreco(BigDecimal.valueOf(145));
        livro.setTitulo("BUU!");
        livro.setDataPublicacao(LocalDate.of(1975,10, 31));

        Autor autor = new Autor();
        autor.setNome("Maria");
        autor.setNacionalidade("Brasileiro");
        autor.setDataNascimento(LocalDate.of(1951, 1, 27));

        livro.setAutor(autor);
        livroRepository.save(livro);
    }

    @Test
    void atualizarAutorDoLivro(){
        UUID id = UUID.fromString("8ff9e998-7ac0-4edc-a69d-3907b22df526");
        var livroParaAtualizar = livroRepository.findById(id).orElse(null);

        UUID idAutor = UUID.fromString("d1534dcb-ee3d-46b6-bc10-93dea58b1b17");
        var autorParaAtualizar = autorRepository.findById(idAutor).orElse(null);

        livroParaAtualizar.setAutor(autorParaAtualizar);
        livroRepository.save(livroParaAtualizar);
    }

    @Test
    void deletarById(){
        var id = UUID.fromString("83cf60de-e04d-491f-9a53-5d7ec6ec5a08");
        livroRepository.deleteById(id);
    }

    @Test
    void buscarLivroTest(){
        var id = UUID.fromString("8ff9e998-7ac0-4edc-a69d-3907b22df526");
        Livro livro = livroRepository.findById(id).orElse(null);
        System.out.println("Informações do Livro:");
        System.out.println("Título: " + livro.getTitulo());
        System.out.println("Autor: " + livro.getAutor().getNome());
    }

    @Test
    void listarLivrosAutor(){
        UUID id = UUID.fromString("6f12ece9-cae0-438b-bec4-05528e320d88");
        Autor autor = autorRepository.findById(id).orElse(null);

        List<Livro> livrosDoAutor = livroRepository.findByAutor(autor);
        autor.setLivros(livrosDoAutor);

        autor.getLivros().forEach(System.out::println);
    }

    @Test
    void pesquisaPorTituloTeste(){
        List<Livro> listaLivro = livroRepository.findByTitulo("Natureza II");
        listaLivro.forEach(System.out::println);
    }

    @Test
    void pesquisaPorIsbnTeste(){
        Optional<Livro> livro = livroRepository.findByIsbn("94787-8754");
        livro.ifPresent(System.out::println);
    }

    @Test
    void pesquisaPorTituloEPrecoTeste(){
        List<Livro> listaLivro = livroRepository.findByTituloAndPreco("BUU!", BigDecimal.valueOf(145));
        listaLivro.forEach(System.out::println);
    }

    @Test
    void listarLivrosComJPQL(){
        List<Livro> result = livroRepository.listarTodos();
        result.forEach(System.out::println);
    }

    @Test
    void listarAutoresComJPQL(){
        List<Autor> result = livroRepository.listarAutoresDosLivros();
        result.forEach(System.out::println);
    }

    @Test
    void lsitarGenerosLivrosAutoresBrasileiros(){
        List<String> result = livroRepository.listarGenerosAutoresBrasileiros();
        result.forEach(System.out::println);
    }

    @Test
    void listarPorGeneroQueryParam(){
        List<Livro> livros = livroRepository.findByGenero(GeneroLivro.FICCAO, "dataPublicacao");
        livros.forEach(System.out::println);
    }

    @Test
    void listarPorGeneroPositionalParameters(){
        List<Livro> livros = livroRepository.findByGeneroPositionalParameters(GeneroLivro.FICCAO, "dataPublicacao");
        livros.forEach(System.out::println);
    }

    @Test
    void deletePorGeneroTest(){
        livroRepository.deleteByGenero(GeneroLivro.FANTASIA);
    }

    @Test
    void updateDataPublicacaoTest(){
        livroRepository.updateDataPublicacao(LocalDate.of(2000, 1, 1));
    }


}