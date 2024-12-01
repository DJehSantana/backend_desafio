package com.example.backendDesafioSenai.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CadastroEnderecoDTO {
    private Integer idPessoa;
    private Integer cep;
    private String rua;
    private Integer numero;
    private String cidade;
    private String estado;
}
