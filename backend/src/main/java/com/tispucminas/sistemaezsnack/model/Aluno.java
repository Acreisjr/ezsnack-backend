package com.tispucminas.sistemaezsnack.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Aluno extends Usuario {

    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    @JsonManagedReference
    private Responsavel responsavel;

    @ManyToOne
    @JoinColumn(name = "escolaId", referencedColumnName = "id")
    @JsonManagedReference
    private Escola escola;

    private String nome;

    private String matricula;

    @OneToOne(mappedBy = "aluno", cascade = CascadeType.ALL)
    private Conta conta; 
}
