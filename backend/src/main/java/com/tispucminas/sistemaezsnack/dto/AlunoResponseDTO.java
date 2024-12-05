package com.tispucminas.sistemaezsnack.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AlunoResponseDTO {
    private Long id;
    private String nome;
    private String matricula;
    private String user;
    private Long contaId;
}
