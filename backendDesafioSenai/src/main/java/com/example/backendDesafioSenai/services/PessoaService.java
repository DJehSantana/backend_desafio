package com.example.backendDesafioSenai.services;

import com.example.backendDesafioSenai.dtos.*;
import com.example.backendDesafioSenai.exception.CampoObrigatorioException;
import com.example.backendDesafioSenai.exception.RegistroDuplicadoException;
import com.example.backendDesafioSenai.models.Endereco;
import com.example.backendDesafioSenai.models.Pessoa;
import com.example.backendDesafioSenai.parser.EnderecoParser;
import com.example.backendDesafioSenai.parser.PessoaParser;
import com.example.backendDesafioSenai.repositorys.EnderecoRepository;
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
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private EnderecoParser enderecoParser;

    public Set<ResponseListaPessoaDTO> listarTodasPessoas() {
        return pessoaRepository.findAll().stream()
                .filter(pessoa -> !Objects.isNull(pessoa.getEndereco()))
                .map(pessoa -> {
                    String cidade = pessoa.getEndereco().getCidade() + " / " + pessoa.getEndereco().getEstado();
                    return new ResponseListaPessoaDTO(pessoa, cidade);
                }).collect(Collectors.toSet());
    }

    public ResponsePessoaDTO buscarPessoaPorCpf(String cpf) {
        if(!pessoaRepository.existsByCpf(cpf)) return new ResponsePessoaDTO("Não existe ninguém cadastrado com o CPF: " + cpf);
        Pessoa pessoa = pessoaRepository.findFirstByCpf(cpf).orElse(null);

        EnderecoDTO enderecoDTO = new EnderecoDTO(Objects.requireNonNull(pessoa).getEndereco());

        return new ResponsePessoaDTO(pessoa, enderecoDTO);
    }

    public ResponsePessoaDTO buscarPessoaPorId(Integer idPessoa) {

        Pessoa pessoa = pessoaRepository.findById(idPessoa).orElse(null);

        if(Objects.isNull(pessoa)) return new ResponsePessoaDTO("Pesoa não encontrada");

        EnderecoDTO enderecoDTO = new EnderecoDTO(Objects.requireNonNull(pessoa).getEndereco());

        return new ResponsePessoaDTO(pessoa, enderecoDTO);
    }

    public ResponsePessoaDTO cadastrarAtualizarPessoa(RequestPessoaDTO reqPessoaDTO) {

        validarDadosPessoa(reqPessoaDTO);
        validarDadosEndereco(reqPessoaDTO.endereco());

        Pessoa pessoa = pessoaParser.requestPessoaDTOToEntity(reqPessoaDTO);
        Pessoa pessoaAtualizada = pessoaRepository.save(pessoa);

        EnderecoDTO enderecoDTO = reqPessoaDTO.endereco();
        if(!Objects.isNull(enderecoDTO)) {
            CadastroEnderecoDTO cadastroEnderecoDTO = CadastroEnderecoDTO.builder()
                    .idPessoa(Objects.isNull(pessoa.getIdPessoa()) ? null : pessoa.getIdPessoa())
                    .cep(enderecoDTO.cep())
                    .rua(enderecoDTO.rua())
                    .numero(enderecoDTO.numero())
                    .cidade(enderecoDTO.cidade())
                    .estado(enderecoDTO.estado())
                    .build();
            Endereco endereco = enderecoParser.cadastroEnderecoDTOToEntity(cadastroEnderecoDTO);

            endereco.setPessoa(pessoaAtualizada);
            pessoaAtualizada.setEndereco(endereco);
        }

        if(Objects.isNull(pessoaAtualizada.getEndereco())) {
            Endereco endereco = enderecoRepository.findById(pessoaAtualizada.getIdPessoa()).orElse(null);
            pessoaAtualizada.setEndereco(Objects.isNull(endereco) ? new Endereco() : endereco);
        }
        return new ResponsePessoaDTO(pessoaAtualizada, new EnderecoDTO(pessoaAtualizada.getEndereco()));

    }

    private void validarDadosEndereco(EnderecoDTO enderecoDTO) {

        if (Objects.isNull(enderecoDTO) || Objects.isNull(enderecoDTO.cep())) return;

        for (Field field : enderecoDTO.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                // Condição para ignorar o campo idPessoa
                if (field.getName().equals("idPessoa")) continue;

                Object value = field.get(enderecoDTO);

                if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
                    throw new IllegalArgumentException("Todos os campos do endereço devem ser preenchidos quando o CEP é informado.");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Erro ao acessar campo do endereço", e);
            }
        }
    }

    public void excluirPessoaPorId(Integer idPessoa) {
        if(Objects.isNull(idPessoa)) return;

        pessoaRepository.deleteById(idPessoa);
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
}
