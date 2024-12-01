package com.example.backendDesafioSenai.dtos;

import com.example.backendDesafioSenai.models.Pessoa;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponsePessoaDTO(Integer idPessoa, String nome, LocalDate dataNascimento, String cpf, EnderecoDTO endereco, String mensagem) {

    public ResponsePessoaDTO(Pessoa pessoa, EnderecoDTO enderecoDTO) {
        this(pessoa.getIdPessoa(), pessoa.getNome(), pessoa.getNascimento(), pessoa.getCpf(), enderecoDTO, null);
    }

    public ResponsePessoaDTO(String msg) {
        this(null, null, null, null, null, msg);
    }

}
