package com.example.backendDesafioSenai.services;

import com.example.backendDesafioSenai.dtos.*;
import com.example.backendDesafioSenai.exception.CampoObrigatorioException;
import com.example.backendDesafioSenai.exception.RegistroDuplicadoException;
import com.example.backendDesafioSenai.models.Pessoa;
import com.example.backendDesafioSenai.parser.PessoaParser;
import com.example.backendDesafioSenai.repositorys.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PessoaService {

    @Autowired
    PessoaRepository pessoaRepository;

    @Autowired
    PessoaParser pessoaParser;

    @Autowired
    EnderecoService enderecoService;

    public Set<ResponseListaPessoaDTO> listarTodasPessoas() {
        return pessoaRepository.findAll().stream()
                .filter(pessoa -> !Objects.isNull(pessoa.getEndereco()))
                .map(pessoa -> {
                    String cidade = pessoa.getEndereco().getCidade() + " / " + pessoa.getEndereco().getEstado();
                    return new ResponseListaPessoaDTO(pessoa, cidade);
                }).collect(Collectors.toSet());
    }

    public ResponsePessoaDTO buscarPessoaPorCpf(String cpf) {
        if(!pessoaRepository.existsByCpf(cpf)) return new ResponsePessoaDTO("Pessoa não encontrada!");
        Pessoa pessoa = pessoaRepository.findFirstByCpf(cpf).orElse(null);

        EnderecoDTO enderecoDTO = new EnderecoDTO(Objects.requireNonNull(pessoa).getEndereco());

        return new ResponsePessoaDTO(pessoa, enderecoDTO);
    }

    public ResponsePessoaDTO cadastrarAtualizarPessoa(RequestPessoaDTO reqPessoaDTO) {

        validarDadosPessoa(reqPessoaDTO);

        Pessoa pessoa = pessoaParser.requestPessoaDTOToEntity(reqPessoaDTO);

        Pessoa pessoaAtualizada = pessoaRepository.save(pessoa);

        boolean isAtualizacaoEndereco = validarAtualizacaoEndereco(reqPessoaDTO, pessoaAtualizada);

        if(isAtualizacaoEndereco) {
            EnderecoDTO enderecoAtualizadoDTO = enderecoService.cadastrarAtualizarEndereco(reqPessoaDTO.endereco(), pessoaAtualizada);
            return new ResponsePessoaDTO(pessoaAtualizada, enderecoAtualizadoDTO);
        }

        EnderecoDTO enderecoDTO = new EnderecoDTO(pessoaAtualizada.getEndereco());
        return new ResponsePessoaDTO(pessoaAtualizada, enderecoDTO);
    }

    private boolean validarAtualizacaoEndereco(RequestPessoaDTO reqPessoaDTO, Pessoa pessoa) {
        if(Objects.isNull(reqPessoaDTO.endereco())) return false;

        // Verifica se o ID da pessoa no endereço do reqPessoaDTO corresponde ao ID da pessoaDTO
        if (!reqPessoaDTO.endereco().idPessoa().equals(pessoa.getIdPessoa())) {
            throw new IllegalArgumentException("ID da pessoa no endereço divergente de dado salvo no banco!");
        }

        validarDadosEndereco(reqPessoaDTO.endereco());

        return true;

    }

    private void validarDadosPessoa(RequestPessoaDTO pessoaDTO) {
        if(pessoaRepository.existsByCpf(pessoaDTO.cpf()) && Objects.isNull(pessoaDTO.idPessoa())) {
            throw new RegistroDuplicadoException("CPF", pessoaDTO.cpf());
        }

        String[] nomes = pessoaDTO.nome().trim().split("\\s+");
        if (nomes.length < 2) {
            throw new IllegalArgumentException("Nome deve ter mais de um nome");
        }

        for (String n : nomes) {
            if (!Pattern.matches("^[A-Z][a-z]*$", n)) {
                throw new IllegalArgumentException("A primeira letra de cada nome deve ser maiúscula, e as demais minúsculas");
            }
        }

        if (pessoaDTO.dataNascimento() != null && pessoaDTO.dataNascimento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento deve ser uma data futura");
        }
    }

    private void validarDadosEndereco(EnderecoDTO enderecoDTO) {
        if(Objects.isNull(enderecoDTO.idPessoa())) {
            throw new CampoObrigatorioException("ID pessoa");
        }

        if(Objects.isNull(enderecoDTO.cep())) return;

        for (Field field : enderecoDTO.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(enderecoDTO);

                if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
                    throw new IllegalArgumentException("Todos os campos do endereço devem ser preenchidos quando o CEP é informado.");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Erro ao acessar campo do endereço", e);
            }
        }
    }
}
