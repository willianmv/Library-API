package com.example.libraryapi.exception;

public class RegistroDuplicadoException extends RuntimeException{

    public RegistroDuplicadoException(String message) {
        super(message);
    }
}
