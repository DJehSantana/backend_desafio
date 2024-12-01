package com.example.backendDesafioSenai.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PessoaDTO {
    private Integer idPessoa;

    private String nome;

    private LocalDate nascimento;

    private String cpf;

    private EnderecoDTO enderecoDTO;

}
