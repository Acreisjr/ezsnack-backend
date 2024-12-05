package com.tispucminas.sistemaezsnack.model.Enums;

public enum TipoItemCantinaEnum {

    SALGADO("Salgado"),
    DOCE("Doce"),
    BEBIDA("Bebida");

    private final String descricao;

    TipoItemCantinaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}