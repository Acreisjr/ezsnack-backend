package com.tispucminas.sistemaezsnack.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePedidoDTO {
    private Long contaId;
    private List<ItemPedidoDTO> itens;
}
