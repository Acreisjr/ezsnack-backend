package com.tispucminas.sistemaezsnack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginDTO {
    private String user;
    private String senha;
}