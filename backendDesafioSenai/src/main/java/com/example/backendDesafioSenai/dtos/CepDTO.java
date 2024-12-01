package com.example.backendDesafioSenai.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CepDTO {
    private Integer id;
    private Integer cep;
    private String rua;
    private String cidade;
    private String estado;
    private String mensagem;
}
