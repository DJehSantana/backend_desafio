package com.example.backendDesafioSenai.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Table(name="pessoa")
@Entity(name = "pessoa")
@Data
public class Pessoa {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer idPessoa;

        private String nome;

        private LocalDate nascimento;

        private String cpf;

        //Relacionamento bidirecional com Endereco
        @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL)
        private Endereco endereco;
}
