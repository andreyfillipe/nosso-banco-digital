package com.andreyfillipe.nossobancodigital.repository;

import com.andreyfillipe.nossobancodigital.entity.Proposta;
import com.andreyfillipe.nossobancodigital.entity.enums.StatusProposta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PropostaRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    PropostaRepository propostaRepository;

    @Test
    @DisplayName("Filtrar por id")
    public void filtrarPorIdTest() {
        Proposta proposta = criarProposta();
        entityManager.persist(proposta);

        Optional<Proposta> entity = propostaRepository.findById(proposta.getId());

        assertThat(entity.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Retorna verdadeiro quando existir uma proposta na base com o email já cadastrado")
    public void existeEmailJaCadastradoTest() {
        Proposta proposta = criarProposta();
        entityManager.persist(proposta);

        boolean existe = propostaRepository.existsByEmail(proposta.getEmail());

        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("Retorna falso quando não existir uma proposta na base com o email já cadastrado")
    public void naoExisteEmailJaCadastradoTest() {
        boolean existe = propostaRepository.existsByEmail("email@email.com.br");

        assertThat(existe).isFalse();
    }

    @Test
    @DisplayName("Retorna verdadeiro quando existir uma proposta na base com o cpf já cadastrado")
    public void existeCpfJaCadastradoTest() {
        Proposta proposta = criarProposta();
        entityManager.persist(proposta);

        boolean existe = propostaRepository.existsByCpf(proposta.getCpf());

        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("Retorna falso quando não existir uma proposta na base com o cpf já cadastrado")
    public void naoExisteCpfJaCadastradoTest() {
        boolean existe = propostaRepository.existsByEmail("12345678909");

        assertThat(existe).isFalse();
    }

    @Test
    @DisplayName("Retorna verdadeiro quando existir uma proposta na base com o status já cadastrado")
    public void existeStatusJaCadastradoTest() {
        Proposta proposta = criarProposta();
        entityManager.persist(proposta);

        boolean existe = propostaRepository.existsByStatus(proposta.getStatus());

        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("Retorna falso quando não existir uma proposta na base com o status já cadastrado")
    public void naoExisteStatusJaCadastradoTest() {
        boolean existe = propostaRepository.existsByStatus(StatusProposta.LIBERADA);

        assertThat(existe).isFalse();
    }

    private Proposta criarProposta() {
        return Proposta.builder()
                       .nome("nome")
                       .sobrenome("sobrenome")
                       .email("email@email.com.br")
                       .cpf("12345678909")
                       .dataNascimento(LocalDate.of(1990, 10, 20))
                       .status(StatusProposta.LIBERADA)
                       .build();
    }
}
