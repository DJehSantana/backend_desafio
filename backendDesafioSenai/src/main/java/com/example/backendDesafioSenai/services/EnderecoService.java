package com.example.backendDesafioSenai.services;

import com.example.backendDesafioSenai.dtos.EnderecoDTO;
import com.example.backendDesafioSenai.models.Endereco;
import com.example.backendDesafioSenai.models.Pessoa;
import com.example.backendDesafioSenai.parser.EnderecoParser;
import com.example.backendDesafioSenai.repositorys.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    EnderecoParser enderecoParser;

    public EnderecoDTO cadastrarAtualizarEndereco(EnderecoDTO enderecoDTO, Pessoa pessoa) {
        Endereco endereco = enderecoParser.enderecoDTOToEntity(enderecoDTO);

        endereco.setPessoa(pessoa);

        return new EnderecoDTO(enderecoRepository.save(endereco));
    }

}
