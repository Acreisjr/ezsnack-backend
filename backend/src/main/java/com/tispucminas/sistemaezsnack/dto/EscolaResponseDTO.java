package com.tispucminas.sistemaezsnack.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EscolaResponseDTO {
    private Long id;
    private String nome;
    private String cnpj;
    private String telefone;
    private String estado;
    private String cidade;
}
