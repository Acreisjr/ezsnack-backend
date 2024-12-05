package com.tispucminas.sistemaezsnack.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Responsavel extends Usuario {

    @OneToMany(mappedBy = "responsavel")
    @JsonBackReference
    private List<Aluno> alunos;

    private String nome;

    private String cpf;

    @OneToMany(mappedBy = "responsavel")
    private List<Conta> contas = new ArrayList<>();

}