package com.example.backendDesafioSenai.client.service;

import com.example.backendDesafioSenai.dtos.*;

import com.example.backendDesafioSenai.exception.ClientErrorException;
import com.example.backendDesafioSenai.exception.ErrorResponse;
import com.example.backendDesafioSenai.exception.ServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.View;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApiIntegracaoService {
    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(ApiIntegracaoService.class);
    private final View error;

    public ApiIntegracaoService(WebClient.Builder builder, @Value("${api.conta.base-url}") String baseUrl, View error) {
        this.webClient = builder
                .baseUrl(baseUrl)
                .filter(logRequest())
                .build();
        this.error = error;
    }

    public Mono<List<CepDTO>> consultarCepsCadastrados() {
        return webClient.get()
                .uri("cep")
                .retrieve()
                .bodyToFlux(CepDTO.class)
                .collectList()
                .doOnNext(ceps -> System.out.println("CEPs recebidos: " + ceps))
                .doOnError(error -> System.err.println("Erro ao consultar CEPs: " + error.getMessage()));
    }

    public Mono<Set<ContaDTO>> consultarTodasContas() {
        return webClient.get()
                .uri("contas")
                .retrieve()
                .bodyToFlux(ContaDTO.class)
                .collect(Collectors.toSet())
                .doOnNext(contas -> System.out.println("Contas recebidas: " + contas))
                .doOnError(error -> System.err.println("Erro ao consultar contas: " + error.getMessage()));
    }

    public Mono<Set<ContaDTO>> consultarContasPorCpf(String cpf) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("contas/{cpf}")
                        .build(cpf))
                .retrieve()
                .bodyToFlux(ContaDTO.class)
                .collect(Collectors.toSet())
                .doOnNext(contas -> System.out.println("Contas recebidas: " + contas))
                .doOnError(error -> System.err.println("Erro ao consultar contas: " + error.getMessage()));
    }

//    public Mono<Object> criarConta(RequestContaDTO requestContaDTO) {
//        return webClient.post() .uri("/conta") .body(Mono.just(requestContaDTO), RequestContaDTO.class)
//                .retrieve()
//                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
//                        clientResponse -> {
//                    return clientResponse.bodyToMono(ErrorResponse.class)
//                            .flatMap(errorResponse -> {
//                                HttpStatus.valueOf(clientResponse.statusCode().value());
//                                return Mono.error(new WebClientResponseException(
//                                            clientResponse.statusCode().value(),
//                                            clientResponse.statusCode().getReasonPhrase(),
//                                            clientResponse.headers().asHttpHeaders(), null, null ));
//                            });
//                })
//                .bodyToMono(ResponseContaDTO.class)
//                .cast(Object.class)
//                .onErrorResume(WebClientResponseException.class, ex -> {
//                    ErrorResponse errorResponse = new ErrorResponse(ex.getRawStatusCode(), ex.getMessage(), LocalDateTime.now());
//                    return Mono.just(errorResponse);
//                });
//    }

    public Mono<Object> criarConta(RequestContaDTO requestContaDTO) {
        return webClient.post()
                .uri("/conta")
                .body(Mono.just(requestContaDTO), RequestContaDTO.class)
                .retrieve()
                //tratamento separados para erros 4xx e 5xx - recuperacao do status e mensagem de erro da api
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(ErrorResponse.class)
                                .flatMap(errorResponse -> Mono.error(new ClientErrorException(
                                        clientResponse.statusCode().value(),
                                        errorResponse.getMessage() != null
                                                ? errorResponse.getMessage()
                                                : "Erro de cliente ao criar conta",
                                        errorResponse
                                )))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        clientResponse.bodyToMono(ErrorResponse.class)
                                .flatMap(errorResponse -> Mono.error(new ServerErrorException(
                                        clientResponse.statusCode().value(),
                                        errorResponse.getMessage() != null
                                                ? errorResponse.getMessage()
                                                : "Erro interno do servidor ao criar conta",
                                        errorResponse
                                )))
                )
                .bodyToMono(ContaDTO.class)
                //cast do ResponseContaDTO para Object
                .cast(Object.class)
                //em caso de erro, verifica o tipo do erro e converte para as classes de exceção personalizadas
                .onErrorResume(ex -> {
                    if (ex instanceof ClientErrorException clientError) {
                        ErrorResponse errorResponse = new ErrorResponse(
                                clientError.getStatusCode(),
                                clientError.getMessage(),
                                LocalDateTime.now()
                        );
                        return Mono.just(errorResponse);
                    }

                    if (ex instanceof ServerErrorException serverError) {
                        ErrorResponse errorResponse = new ErrorResponse(
                                serverError.getStatusCode(),
                                serverError.getMessage(),
                                LocalDateTime.now()
                        );
                        return Mono.just(errorResponse);
                    }

                    // Tratamento para erros genéricos
                    ErrorResponse errorResponse = new ErrorResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Erro inesperado ao criar conta",
                            LocalDateTime.now()
                    );
                    return Mono.just(errorResponse);
                });
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            return Mono.just(clientRequest);
        });
    }
}