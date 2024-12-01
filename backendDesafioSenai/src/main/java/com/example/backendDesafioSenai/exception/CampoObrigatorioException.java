package com.example.backendDesafioSenai.exception;

public class CampoObrigatorioException extends RuntimeException {

    public CampoObrigatorioException(String nomeCampo) {
        super(String.format("O(s) campo(s) %s não pode(m) estar vazio(s)!", nomeCampo));
    }
}
