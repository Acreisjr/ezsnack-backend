package com.tispucminas.sistemaezsnack.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ContaResponseDTO {
    private Long id;
    private BigDecimal saldo;
    private Long responsavelId;
    private Long alunoId;
    private BigDecimal limiteDiario;
    private BigDecimal limiteDiarioPadrao;
    private boolean temLimiteDiario; 
}
