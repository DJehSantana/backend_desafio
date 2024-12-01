package com.example.backendDesafioSenai.exception;

public class CadastroContaException extends RuntimeException{
    private final int statusCode;

    public CadastroContaException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
