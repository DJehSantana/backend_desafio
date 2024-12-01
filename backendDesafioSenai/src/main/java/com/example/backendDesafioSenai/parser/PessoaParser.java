package com.example.backendDesafioSenai.parser;

import com.example.backendDesafioSenai.dtos.EnderecoDTO;
import com.example.backendDesafioSenai.dtos.PessoaDTO;
import com.example.backendDesafioSenai.dtos.RequestPessoaDTO;
import com.example.backendDesafioSenai.models.Pessoa;
import org.springframework.stereotype.Component;

@Component
public class PessoaParser {

    public Pessoa requestPessoaDTOToEntity(RequestPessoaDTO dto) {
        Pessoa entidade = new Pessoa();
        entidade.setIdPessoa(dto.idPessoa());
        entidade.setCpf(dto.cpf());
        entidade.setNome(dto.nome());
        entidade.setNascimento(dto.dataNascimento());

        return entidade;
    }

    public PessoaDTO entityToPessoaDTO(Pessoa entidade) {
        return PessoaDTO.builder()
                .idPessoa(entidade.getIdPessoa())
                .cpf(entidade.getCpf())
                .nome(entidade.getNome())
                .nascimento(entidade.getNascimento())
                .enderecoDTO(new EnderecoDTO(entidade.getEndereco()))
                .build();
    }
}
