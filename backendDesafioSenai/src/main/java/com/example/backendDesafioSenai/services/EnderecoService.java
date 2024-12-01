package com.example.backendDesafioSenai.services;

import com.example.backendDesafioSenai.client.service.ApiIntegracaoService;
import com.example.backendDesafioSenai.dtos.CepDTO;
import com.example.backendDesafioSenai.dtos.EnderecoDTO;
import com.example.backendDesafioSenai.dtos.RequestPessoaDTO;
import com.example.backendDesafioSenai.exception.CampoObrigatorioException;
import com.example.backendDesafioSenai.models.Endereco;
import com.example.backendDesafioSenai.models.Pessoa;
import com.example.backendDesafioSenai.parser.EnderecoParser;
import com.example.backendDesafioSenai.repositorys.EnderecoRepository;
import com.example.backendDesafioSenai.repositorys.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@Service
public class EnderecoService {
    private final ApiIntegracaoService apiIntegracaoService;
    private final EnderecoRepository enderecoRepository;
    private final EnderecoParser enderecoParser;
    private final PessoaRepository pessoaRepository;


    @Autowired
    public EnderecoService(ApiIntegracaoService apiIntegracaoService,
                           EnderecoRepository enderecoRepository,
                           EnderecoParser enderecoParser, PessoaRepository pessoaRepository) {

        this.apiIntegracaoService = apiIntegracaoService;
        this.enderecoRepository = enderecoRepository;
        this.enderecoParser = enderecoParser;
        this.pessoaRepository = pessoaRepository;
    }

    public Endereco cadastrarAtualizarEndereco(EnderecoDTO enderecoDTO, Integer idPessoa) {
        //validarDadosEndereco(enderecoDTO);

        Endereco endereco = enderecoParser.enderecoDTOToEntity(enderecoDTO);
        Pessoa pessoaBD = pessoaRepository.findByIdPessoa(idPessoa).orElse(null);

        if(Objects.isNull(pessoaBD)) throw new IllegalArgumentException("Pessoa não encontrada");

        endereco.setPessoa(pessoaBD);

        return enderecoRepository.save(endereco);
    }

    public Mono<List<CepDTO>> consultarCepsApiExterna() {
        return apiIntegracaoService.consultarCepsCadastrados();
    }



}
