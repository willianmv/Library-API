package com.example.libraryapi.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "TB_USUARIO")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String login;

    private String senha;

    @OneToMany(mappedBy = "usuario")
    private Set<Autor> autoresCadastrados;

    @OneToMany(mappedBy = "usuario")
    private Set<Livro> livrosCadastrados;

    @Type(ListArrayType.class)
    @Column(name = "roles", columnDefinition = "varchar[]")
    private List<String> roles;
}
