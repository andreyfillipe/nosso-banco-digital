package com.andreyfillipe.nossobancodigital.repository;

import com.andreyfillipe.nossobancodigital.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

    boolean existsById(Integer id);
}
