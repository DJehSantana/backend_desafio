package com.example.backendDesafioSenai.dtos;

import com.example.backendDesafioSenai.models.Endereco;

public record EnderecoDTO(Integer idPessoa, Integer cep, String rua, Integer numero, String cidade, String estado) {
    public EnderecoDTO(Endereco endereco) {
        this(endereco.getIdPessoa(), endereco.getCep(), endereco.getRua(), endereco.getNumero(), endereco.getCidade(), endereco.getEstado());
    }
}
