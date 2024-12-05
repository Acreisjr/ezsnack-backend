package com.tispucminas.sistemaezsnack.model.Enums;

public enum PedidoStatusEnum {

    PENDENTE("Pendente"),
    APROVADO("Aprovado"),
    ENTREGUE("Entregue"),
    NEGADO("Negado"),
    CANCELADO("Cancelado");
    
    private final String descricao;

    PedidoStatusEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}