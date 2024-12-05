package com.tispucminas.sistemaezsnack.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioUpdateDTO {
    private String senhaAtual;
    private String novaSenha;
}
