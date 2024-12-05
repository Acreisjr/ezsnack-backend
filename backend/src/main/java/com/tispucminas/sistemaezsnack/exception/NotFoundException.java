package com.tispucminas.sistemaezsnack.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotFoundException extends Exception{
    private String message;
}
