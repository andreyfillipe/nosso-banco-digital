package com.andreyfillipe.nossobancodigital.repository;

import com.andreyfillipe.nossobancodigital.entity.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArquivoRepository extends JpaRepository<Arquivo, Integer> {

    @Query("select case when count(a) > 0 then true else false end from Arquivo a join a.proposta p where p.id = :id")
    boolean existsByProposta(Integer id);
}
