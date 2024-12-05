package com.tispucminas.sistemaezsnack.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IllegalOperationException extends Exception{
    private String message;
}
