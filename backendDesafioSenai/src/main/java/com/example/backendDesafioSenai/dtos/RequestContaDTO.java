package com.example.backendDesafioSenai.dtos;

import jakarta.validation.constraints.*;

public record RequestContaDTO(
        @NotBlank(message = "Número da conta não pode estar em branco")
        @Pattern(regexp = "^\\d{6}-\\d$", message = "Número da conta deve estar no formato 000000-0")
                String conta,
        @NotNull(message = "CPF é obrigatório")
        @Min(value = 10000000000L, message = "CPF deve ter 11 dígitos")
        @Max(value = 99999999999L, message = "CPF deve ter 11 dígitos")
        Long cpf) {
}
