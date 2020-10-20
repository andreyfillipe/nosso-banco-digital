package com.andreyfillipe.nossobancodigital.repository;

import com.andreyfillipe.nossobancodigital.entity.Proposta;
import com.andreyfillipe.nossobancodigital.entity.enums.StatusProposta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropostaRepository extends JpaRepository<Proposta, Integer> {

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsByStatus(StatusProposta status);
}
