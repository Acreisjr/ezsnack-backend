package com.tispucminas.sistemaezsnack.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AdminEscola extends Usuario {

    @ManyToOne
    @JoinColumn(name = "escolaId")
    private Escola escola;
}