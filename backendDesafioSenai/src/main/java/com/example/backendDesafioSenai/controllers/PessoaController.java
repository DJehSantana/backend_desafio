package com.example.backendDesafioSenai.controllers;

import com.example.backendDesafioSenai.dtos.RequestPessoaDTO;
import com.example.backendDesafioSenai.dtos.ResponseListaPessoaDTO;
import com.example.backendDesafioSenai.dtos.ResponsePessoaDTO;
import com.example.backendDesafioSenai.repositorys.PessoaRepository;
import com.example.backendDesafioSenai.services.PessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("pessoa")
public class PessoaController {
    private final PessoaService pessoaService;

    @Autowired
    PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @GetMapping
    public ResponseEntity<Set<ResponseListaPessoaDTO>> getAll() {
        Set<ResponseListaPessoaDTO> responseLista = pessoaService.listarTodasPessoas();
        return ResponseEntity.ok(responseLista);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<ResponsePessoaDTO> getByCpf(@PathVariable("cpf") String cpf)  {
        ResponsePessoaDTO response = pessoaService.buscarPessoaPorCpf(cpf);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{idPessoa}")
    public ResponseEntity<ResponsePessoaDTO> getByIdPessoa(@PathVariable("idPessoa") Integer idPessoa)  {
        ResponsePessoaDTO response = pessoaService.buscarPessoaPorId(idPessoa);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponsePessoaDTO> saveOrUpdate(@Valid @RequestBody RequestPessoaDTO requestPessoaDTO) {
        ResponsePessoaDTO response = pessoaService.cadastrarAtualizarPessoa(requestPessoaDTO);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{idPessoa}")
    public ResponseEntity<Object> deleteById(@PathVariable("idPessoa") Integer idPessoa) {
        pessoaService.excluirPessoaPorId(idPessoa);

        return ResponseEntity.noContent().build();
    }
}
