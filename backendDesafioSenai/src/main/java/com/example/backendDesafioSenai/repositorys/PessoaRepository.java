package com.example.backendDesafioSenai.repositorys;

import com.example.backendDesafioSenai.models.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {

    Optional<Pessoa> findFirstByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
