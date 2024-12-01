package com.example.backendDesafioSenai.models;

import jakarta.persistence.*;
import lombok.Data;

@Table(name="endereco")
@Entity(name = "endereco")
@Data
public class Endereco {

    @Id
    private Integer idPessoa;

    private Integer cep;

    private String rua;

    private Integer numero;

    private String cidade;

    private String estado;

    // Relacionamento com Pessoa
    @MapsId
    @OneToOne
    @JoinColumn(name = "ID_PESSOA")
    private Pessoa pessoa;
}
