package com.tispucminas.sistemaezsnack.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EscolaCreateDTO {
    private String nome;
    private String cnpj;
    private String telefone;
    private String estado;
    private String cidade;
}
