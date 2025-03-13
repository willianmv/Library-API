package com.example.libraryapi.controller.common;

import com.example.libraryapi.controller.dto.ErroCampo;
import com.example.libraryapi.controller.dto.ErroResposta;
import com.example.libraryapi.exception.CampoInvalidoException;
import com.example.libraryapi.exception.OperacaoNaoPermitidaException;
import com.example.libraryapi.exception.RegistroDuplicadoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("Erro de validação: {}", e.getMessage());
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<ErroCampo> camposErro = fieldErrors
                .stream()
                .map(erro -> new ErroCampo(erro.getField(), erro.getDefaultMessage()))
                .toList();
        return new ErroResposta(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação", camposErro);
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResposta handleRegistroDuplicadoException(RegistroDuplicadoException e){
        return ErroResposta.conflito(e.getMessage());
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta handleOperacaoNaoPermitidaException(OperacaoNaoPermitidaException e){
        return ErroResposta.respostaPadrao(e.getMessage());
    }

    @ExceptionHandler(CampoInvalidoException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleCampoInvalidoException(CampoInvalidoException e){
        return new ErroResposta(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação", List.of(new ErroCampo(e.getCampo(), e.getMessage())));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErroResposta handleAccessDeniedException(AccessDeniedException e){
        return new ErroResposta(HttpStatus.FORBIDDEN.value(), "Acesso Negado.", List.of());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta handleErrosNaoTratadosException(RuntimeException e){
        log.error("Erro inesperado: ", e);
        return new ErroResposta(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro inesperado. Entre em contato com a administração. ", List.of());
    }

}
