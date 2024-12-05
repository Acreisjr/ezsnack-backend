package com.tispucminas.sistemaezsnack.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCantinaUpdateDTO {

    private String nome;
    private String tipo; // PS: Enviar dentre as opções do enum
    private Boolean disponibilidade;
    private BigDecimal preco;
}