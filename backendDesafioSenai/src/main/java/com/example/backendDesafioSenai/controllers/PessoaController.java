package com.example.backendDesafioSenai.controllers;

import com.example.backendDesafioSenai.dtos.RequestPessoaDTO;
import com.example.backendDesafioSenai.dtos.ResponsePessoaDTO;
import com.example.backendDesafioSenai.services.PessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Set;

@RestController
@RequestMapping("pessoa")
public class PessoaController {
    private final PessoaService pessoaService;

    @Autowired
    PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }
    /**
     * Recupera todas as pessoas cadastradas no sistema.
     *
     * @return ResponseEntity contendo um conjunto de DTOs de pessoas
     */
    @GetMapping
    public ResponseEntity<Set<ResponsePessoaDTO>> getAll() {
        Set<ResponsePessoaDTO> responseLista = pessoaService.listarTodasPessoas();
        return ResponseEntity.ok(responseLista);
    }

    /**
     * Busca uma pessoa específica pelo seu CPF.
     *
     * @param cpf CPF da pessoa a ser buscada
     * @return ResponseEntity contendo o DTO da pessoa encontrada
     */
    @GetMapping("/{cpf}")
    public ResponseEntity<ResponsePessoaDTO> getByCpf(@PathVariable("cpf") String cpf)  {
        ResponsePessoaDTO response = pessoaService.buscarPessoaPorCpf(cpf);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca uma pessoa específica pelo seu ID.
     *
     * @param idPessoa Identificador único da pessoa
     * @return ResponseEntity contendo o DTO da pessoa encontrada
     */
    @GetMapping("/{idPessoa}")
    public ResponseEntity<ResponsePessoaDTO> getByIdPessoa(@PathVariable("idPessoa") Integer idPessoa)  {
        ResponsePessoaDTO response = pessoaService.buscarPessoaPorId(idPessoa);
        return ResponseEntity.ok(response);
    }

    /**
     * Cadastra uma nova pessoa ou atualiza uma pessoa existente.
     *
     * @param requestPessoaDTO DTO com os dados da pessoa a ser cadastrada/atualizada
     * @return ResponseEntity contendo o DTO da pessoa cadastrada/atualizada
     */
    @PostMapping
    public ResponseEntity<ResponsePessoaDTO> saveOrUpdate(@Valid @RequestBody RequestPessoaDTO requestPessoaDTO) {
        ResponsePessoaDTO response = pessoaService.cadastrarAtualizarPessoa(requestPessoaDTO);

        return ResponseEntity.ok(response);
    }

    /**
     * Exclui uma pessoa do sistema pelo seu ID.
     *
     * @param idPessoa Identificador único da pessoa a ser excluída
     * @return ResponseEntity sem conteúdo (204 No Content)
     */
    @DeleteMapping("/{idPessoa}")
    public ResponseEntity<Object> deleteById(@PathVariable("idPessoa") Integer idPessoa) {
        pessoaService.excluirPessoaPorId(idPessoa);

        return ResponseEntity.noContent().build();
    }
}
