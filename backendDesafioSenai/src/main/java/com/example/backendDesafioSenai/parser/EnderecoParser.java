package com.example.backendDesafioSenai.parser;

import com.example.backendDesafioSenai.dtos.CadastroEnderecoDTO;
import com.example.backendDesafioSenai.dtos.EnderecoDTO;
import com.example.backendDesafioSenai.models.Endereco;
import org.springframework.stereotype.Component;

@Component
public class EnderecoParser {

    public Endereco enderecoDTOToEntity(EnderecoDTO dto) {
        Endereco entidade = new Endereco();
        entidade.setIdPessoa(dto.idPessoa());
        entidade.setCep(dto.cep());
        entidade.setRua(dto.rua());
        entidade.setNumero(dto.numero());
        entidade.setCidade(dto.cidade());
        entidade.setEstado(dto.estado());

        return entidade;
    }

    public Endereco cadastroEnderecoDTOToEntity(CadastroEnderecoDTO dto) {
        Endereco entidade = new Endereco();
        entidade.setIdPessoa(dto.getIdPessoa());
        entidade.setCep(dto.getCep());
        entidade.setRua(dto.getRua());
        entidade.setNumero(dto.getNumero());
        entidade.setCidade(dto.getCidade());
        entidade.setEstado(dto.getEstado());

        return entidade;
    }

}
