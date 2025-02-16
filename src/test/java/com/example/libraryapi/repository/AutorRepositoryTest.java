package com.example.libraryapi.repository;

import com.example.libraryapi.model.Autor;
import com.example.libraryapi.model.GeneroLivro;
import com.example.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTest {

    @Autowired
    AutorRepository autorRepository;

    @Autowired
    LivroRepository livroRepository;

    @Test
    public void salvarTest(){
        Autor autor = new Autor();
        autor.setNome("Maria");
        autor.setNacionalidade("Brasileiro");
        autor.setDataNascimento(LocalDate.of(1951, 1, 27));

        var autorSalvo = autorRepository.save(autor);
        System.out.println("Autor Salvo: "+ autorSalvo);
    }

    @Test
    public void atualizarTest(){
        UUID id = UUID.fromString("2bc7c1ce-1140-4c9b-ad3a-f33737063a2c");
        Optional<Autor> possivelAutor = autorRepository.findById(id);

        if(possivelAutor.isPresent()){
            Autor autorEncontrado = possivelAutor.get();
            System.out.println("Dados do Autor:");
            System.out.println(autorEncontrado);

            autorEncontrado.setNome("Pedro");
            autorEncontrado.setDataNascimento(LocalDate.of(1960, 5, 30));
            autorRepository.save(autorEncontrado);
        }
    }

    @Test
    public void listarTodos(){
        List<Autor> autores = autorRepository.findAll();
        autores.forEach(System.out::println);
    }

    @Test
    public void countTest(){
        System.out.println("Contagem de autores: "+ autorRepository.count());
    }

    @Test
    public void deleteByIdTest(){
        var id = UUID.fromString("ab9a8a95-6420-43b0-910e-05ea907dd811");
        autorRepository.deleteById(id);
    }

    @Test
    public void deleteByObjectTest(){
        var id = UUID.fromString("2bc7c1ce-1140-4c9b-ad3a-f33737063a2c");
        var autorDoBanco = autorRepository.findById(id).get();
        autorRepository.delete(autorDoBanco);
    }

    @Test
    void salvarAutorComLivrosTest(){
        Autor autor = new Autor();
        autor.setNome("Lucas");
        autor.setNacionalidade("Mexicano");
        autor.setDataNascimento(LocalDate.of(1970, 11, 25));
        autor.setLivros( new ArrayList<>());

        Livro livro = new Livro();
        livro.setIsbn("56413-8474");
        livro.setGenero(GeneroLivro.FANTASIA);
        livro.setPreco(BigDecimal.valueOf(165));
        livro.setTitulo("Mundo Fantasia");
        livro.setDataPublicacao(LocalDate.of(1999,4, 7) );
        livro.setAutor(autor);

        Livro livro2 = new Livro();
        livro2.setIsbn("64516-8474");
        livro2.setGenero(GeneroLivro.CIENCIA);
        livro2.setPreco(BigDecimal.valueOf(650));
        livro2.setTitulo("Natureza II");
        livro2.setDataPublicacao(LocalDate.of(2000,11, 27) );
        livro2.setAutor(autor);

        autor.getLivros().add(livro);
        autor.getLivros().add(livro2);

        autorRepository.save(autor);
        livroRepository.saveAll(autor.getLivros());
    }
}
