package com.example.backendDesafioSenai.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContaDTO {
    private Integer idConta;
    private LocalDateTime data;
    private Long cpf;
    private String conta;
    private BigDecimal saldo;
    private String mensagem;
}
