package com.andreyfillipe.nossobancodigital.repository;

import com.andreyfillipe.nossobancodigital.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("select case when count(t) > 0 then true else false end from Token t where t.token = :token and t.expiracao < :data")
    boolean existsTokenExpirado(Integer token, LocalDateTime data);

    boolean existsByTokenAndUtilizado(Integer token, Boolean utilizado);

    Optional<Token> findOneByAndTokenOrderByIdDesc(Integer token);
}
