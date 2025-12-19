package com.example.usuario.infrastructure.exception;

public class ConflictExceptions extends RuntimeException{

    public ConflictExceptions(String mensagem){
        super(mensagem);
    }

    public ConflictExceptions(String mensagem, Throwable throwable){
        super(mensagem);
    }
}
