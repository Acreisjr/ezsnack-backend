package com.tispucminas.sistemaezsnack.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCantinaCreateDTO {
    private String nome;
    private String tipo;
    private Boolean disponibilidade;
    private BigDecimal preco;
    private Long escolaId; 
}
