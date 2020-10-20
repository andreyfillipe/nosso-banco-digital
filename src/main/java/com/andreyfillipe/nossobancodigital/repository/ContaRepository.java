package com.andreyfillipe.nossobancodigital.repository;

import com.andreyfillipe.nossobancodigital.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Integer> {

    @Query("select c from Conta c join c.proposta p where p.email = :email and p.cpf = :cpf")
    Optional<Conta> findByEmailAndCpf(String email, String cpf);

    @Query("select c from Conta c join c.tokens t where t.token = :token")
    Optional<Conta> findByToken(Integer token);

    @Query("select c from Conta c join c.proposta p where c.agencia = :agencia and c.conta = :conta and p.cpf = :cpf")
    Optional<Conta> findByAgenciaAndContaAndCpf(String agencia, String conta, String cpf);

    @Query("select case when count(c) > 0 then true else false end from Conta c join c.proposta p where c.agencia = :agencia and c.conta = :conta and p.cpf = :cpf")
    boolean existsByAgenciaAndContaAndCpf(String agencia, String conta, String cpf);
}
