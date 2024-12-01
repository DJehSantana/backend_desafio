package com.example.backendDesafioSenai.dtos;

import com.example.backendDesafioSenai.models.Pessoa;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseListaPessoaDTO(Integer idPessoa, String nome, String cpf, String cidade, String mensagem) {
    public ResponseListaPessoaDTO(Pessoa pessoa, String cidade) {
        this(pessoa.getIdPessoa(), pessoa.getNome(), pessoa.getCpf(), cidade, null);
    }
}
