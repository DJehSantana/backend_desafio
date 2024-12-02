package com.example.backendDesafioSenai.controllers;


import com.example.backendDesafioSenai.dtos.RequestContaDTO;
import com.example.backendDesafioSenai.dtos.ResponseContaPessoaDTO;
import com.example.backendDesafioSenai.exception.CadastroContaException;
import com.example.backendDesafioSenai.services.ContaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Set;

@RestController
@RequestMapping("conta")
public class ContaController {

    private final ContaService contaService;

    @Autowired
    ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @GetMapping
    public Mono<ResponseEntity<Set<ResponseContaPessoaDTO>>> getAll() {
        return contaService.bucarContasCadastradasApi()
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.ok(Collections.emptySet())));
    }

    @PostMapping
    public Mono<ResponseEntity<ResponseContaPessoaDTO>> saveOrUpdate(@Valid @RequestBody RequestContaDTO requestContaDTO) {
        return contaService.cadastrarAtualizarConta(requestContaDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(CadastroContaException.class, ex ->
                        Mono.just(ResponseEntity.badRequest().build()))
                .onErrorResume(ex ->
                        Mono.just(ResponseEntity.internalServerError().build()));
    }


}
