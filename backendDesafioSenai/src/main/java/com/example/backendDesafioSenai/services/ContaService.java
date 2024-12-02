package com.example.backendDesafioSenai.services;

import com.example.backendDesafioSenai.client.service.ApiIntegracaoService;
import com.example.backendDesafioSenai.dtos.ContaDTO;
import com.example.backendDesafioSenai.dtos.RequestContaDTO;
import com.example.backendDesafioSenai.dtos.ResponseContaPessoaDTO;
import com.example.backendDesafioSenai.exception.CadastroContaException;
import com.example.backendDesafioSenai.exception.ErrorResponse;
import com.example.backendDesafioSenai.models.Pessoa;
import com.example.backendDesafioSenai.repositorys.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ContaService {

    private final ApiIntegracaoService apiIntegracaoService;
    private final PessoaRepository pessoaRepository;

    @Autowired
    ContaService(
            ApiIntegracaoService apiIntegracaoService,
            PessoaRepository pessoaRepository
           ) {
        this.apiIntegracaoService = apiIntegracaoService;
        this.pessoaRepository = pessoaRepository;
    }

    public Mono<Set<ResponseContaPessoaDTO>> bucarContasCadastradasApi() {
        return apiIntegracaoService.consultarTodasContas().map(contas -> contas.stream()
                .map(this::tratarRespostaConta)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));
    }

    private ResponseContaPessoaDTO tratarRespostaConta(ContaDTO contaDTO) {
        String cpfString = contaDTO.getCpf().toString();
        Pessoa pessoa = pessoaRepository.findFirstByCpf(cpfString).orElse(null);

        if(Objects.isNull(pessoa)) return null;

        String cpfFormatado = formatarCpf(pessoa.getCpf());
        return new ResponseContaPessoaDTO(pessoa.getNome(), cpfFormatado, pessoa.getCpf(), contaDTO.getConta());
    }

    public Mono<ResponseContaPessoaDTO> cadastrarAtualizarConta(RequestContaDTO requestContaDTO) {
        return apiIntegracaoService.criarConta(requestContaDTO)
                .flatMap(this::processarResposta)
                .onErrorMap(this::tratarErroInesperado);
    }

    private Mono<ResponseContaPessoaDTO> processarResposta(Object response) {
        if (response instanceof ErrorResponse errorResponse) {
            return Mono.error(new CadastroContaException(
                    errorResponse.getMessage(),
                    errorResponse.getStatus()
            ));
        }

        if (response instanceof ContaDTO contaDTO) {
            ResponseContaPessoaDTO responseDTO = tratarRespostaConta(contaDTO);
            return responseDTO != null ? Mono.just(responseDTO) : Mono.empty();
        }

        return Mono.error(new CadastroContaException(
                "Resposta inesperada da API",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        ));
    }

    private Throwable tratarErroInesperado(Throwable originalError) {
        if (originalError instanceof CadastroContaException) {
            return originalError;
        }

        return new CadastroContaException(
                "Erro inesperado ao criar conta: " + originalError.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
    }

    private String formatarCpf(String cpf) {
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

}
