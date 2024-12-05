package com.tispucminas.sistemaezsnack.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String matricula;
    private EscolaResponseDTO escola;
    private List<AlunoResponseDTO> alunos;
    private String tipo;
    private String cpf;
}
