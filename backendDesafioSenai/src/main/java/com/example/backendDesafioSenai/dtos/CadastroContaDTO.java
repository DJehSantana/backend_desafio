package com.example.backendDesafioSenai.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CadastroContaDTO {
    private RequestContaDTO requestContaDTO;
    private Integer idPessoa;
    private String nomePessoa;
}
