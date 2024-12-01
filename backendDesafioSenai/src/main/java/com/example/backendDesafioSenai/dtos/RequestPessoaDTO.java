package com.example.backendDesafioSenai.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RequestPessoaDTO(
        Integer idPessoa,
        @NotBlank(message = "Campo nome é de preenchimento obrigatório")
        String nome,
        LocalDate dataNascimento,
        @NotBlank(message = "Campo CPF é de preenchimento obrigatório")
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter apenas números e ter exatamente 11 dígitos")
        String cpf,
        EnderecoDTO endereco) {
}
