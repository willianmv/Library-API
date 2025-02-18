package com.example.libraryapi.exception;

import lombok.Getter;

public class CampoInvalidoException extends RuntimeException{

    @Getter
    private String campo;

    public CampoInvalidoException(String campo, String message) {
        super(message);
        this.campo = campo;
    }
}
