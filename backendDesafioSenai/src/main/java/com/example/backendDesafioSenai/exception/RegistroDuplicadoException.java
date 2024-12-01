package com.example.backendDesafioSenai.exception;

import lombok.Getter;

@Getter
public class RegistroDuplicadoException extends RuntimeException {
    private String campo;
    private Object valorDuplicado;

    public RegistroDuplicadoException(String campo, Object valorDuplicado) {
        super(criarMensagem(campo, valorDuplicado));
        this.campo = campo;
        this.valorDuplicado = valorDuplicado;
    }

    private static String criarMensagem(String campo, Object valor) {
        return String.format("Já existe um registro cadastrado com o %s: %s",
                campo, valor);
    }
}
