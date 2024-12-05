package com.tispucminas.sistemaezsnack.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UnauthorizedException extends Exception{
    private String message;
}

