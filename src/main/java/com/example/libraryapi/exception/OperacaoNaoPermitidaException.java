package com.example.libraryapi.exception;

public class OperacaoNaoPermitidaException extends RuntimeException{

    public OperacaoNaoPermitidaException(String message) {
        super(message);
    }
}
