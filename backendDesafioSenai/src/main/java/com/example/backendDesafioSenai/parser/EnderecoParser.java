package com.example.backendDesafioSenai.parser;

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

}
