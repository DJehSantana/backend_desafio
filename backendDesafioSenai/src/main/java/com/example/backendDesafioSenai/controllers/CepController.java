package com.example.backendDesafioSenai.controllers;

import com.example.backendDesafioSenai.dtos.CepDTO;
import com.example.backendDesafioSenai.services.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("cep")
public class CepController {

    private final EnderecoService enderecoService;

    @Autowired
    CepController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @GetMapping
    public Mono<ResponseEntity<List<CepDTO>>> getAll() {
        return enderecoService.consultarCepsApiExterna()
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.ok(Collections.emptyList())));
    }
}
