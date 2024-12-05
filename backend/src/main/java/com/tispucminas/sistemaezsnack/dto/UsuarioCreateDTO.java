package com.tispucminas.sistemaezsnack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioCreateDTO {

    @NotBlank(message = "O nome de usuário é obrigatório")
    private String user;
    
    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    private Long escolaId;

    private Long responsavelId;

    private String nome;

    private String identificacao;

    @NotBlank(message = "O tipo de usuário é obrigatório")
    private String tipo;
}
