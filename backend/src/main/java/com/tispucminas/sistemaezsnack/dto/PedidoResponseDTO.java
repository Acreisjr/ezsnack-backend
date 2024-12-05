package com.tispucminas.sistemaezsnack.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PedidoResponseDTO {
    private Long id;
    private String nomeAluno;
    private BigDecimal precoTotal;
    private LocalDateTime data;
    private String status;
    private List<ItemResponseDTO> itensPedidos;
}
