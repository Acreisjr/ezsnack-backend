package com.tispucminas.sistemaezsnack.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemResponseDTO {
    private String nome;
    private BigDecimal valorTotal;
    private int quantidade;
}
