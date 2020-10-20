package com.andreyfillipe.nossobancodigital.service;

import com.andreyfillipe.nossobancodigital.entity.Proposta;
import com.andreyfillipe.nossobancodigital.entity.dto.AceitePropostaDTO;
import com.andreyfillipe.nossobancodigital.entity.enums.StatusProposta;
import com.andreyfillipe.nossobancodigital.repository.ArquivoRepository;
import com.andreyfillipe.nossobancodigital.repository.EnderecoRepository;
import com.andreyfillipe.nossobancodigital.repository.PropostaRepository;
import com.andreyfillipe.nossobancodigital.service.exception.EntidadeNaoProcessavelException;
import com.andreyfillipe.nossobancodigital.service.exception.NaoEncontradoException;
import com.andreyfillipe.nossobancodigital.service.exception.RegraNegocioException;
import com.andreyfillipe.nossobancodigital.service.impl.PropostaServiceImpl;
import com.andreyfillipe.nossobancodigital.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class PropostaServiceTest {

    PropostaService propostaService;

    @MockBean
    PropostaRepository propostaRepository;

    @MockBean
    EnderecoRepository enderecoRepository;

    @MockBean
    ArquivoRepository arquivoRepository;

    @MockBean
    ContaService contaService;

    @MockBean
    EmailService emailService;

    @MockBean
    Util util;

    @BeforeEach
    public void setUp() {
        this.propostaService = new PropostaServiceImpl(propostaRepository, enderecoRepository, arquivoRepository, contaService, emailService, util);
    }

    @Test
    @DisplayName("Filtrar por id com sucesso")
    public void filtrarPorIdTest() {
        Integer id = 1;
        Proposta proposta = this.criarProposta();
        proposta.setId(id);
        Mockito.when(propostaRepository.findById(id)).thenReturn(Optional.of(proposta));

        Proposta propostaEncontrada = propostaService.filtrarPorId(id);

        assertThat(propostaEncontrada.getId()).isEqualTo(proposta.getId());
        assertThat(propostaEncontrada.getNome()).isEqualTo(proposta.getNome());
        assertThat(propostaEncontrada.getSobrenome()).isEqualTo(proposta.getSobrenome());
        assertThat(propostaEncontrada.getEmail()).isEqualTo(proposta.getEmail());
        assertThat(propostaEncontrada.getCpf()).isEqualTo(proposta.getCpf());
        assertThat(propostaEncontrada.getDataNascimento()).isEqualTo(proposta.getDataNascimento());
    }

    @Test
    @DisplayName("Retornar exceção ao filtrar por id, quando ele não existe na base de dados")
    public void filtrarPorIdNaoEncontradoTest() {
        Integer id = 1;
        Mockito.when(propostaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NaoEncontradoException.class, () -> propostaService.filtrarPorId(id));

        Mockito.verify(propostaRepository, Mockito.times(1)).findById(id);
    }

    @Test
    @DisplayName("Filtrar por proposta com sucesso")
    public void filtrarPropostaTest() {
        Integer id = 1;
        Proposta proposta = this.criarProposta();
        proposta.setId(id);
        Mockito.when(propostaRepository.findById(id)).thenReturn(Optional.of(proposta));
        Mockito.when(enderecoRepository.existsById(id)).thenReturn(true);
        Mockito.when(arquivoRepository.existsByProposta(id)).thenReturn(true);

        Proposta propostaEncontrada = propostaService.filtrarProposta(id);

        assertThat(propostaEncontrada.getId()).isEqualTo(proposta.getId());
        assertThat(propostaEncontrada.getNome()).isEqualTo(proposta.getNome());
        assertThat(propostaEncontrada.getSobrenome()).isEqualTo(proposta.getSobrenome());
        assertThat(propostaEncontrada.getEmail()).isEqualTo(proposta.getEmail());
        assertThat(propostaEncontrada.getCpf()).isEqualTo(proposta.getCpf());
        assertThat(propostaEncontrada.getDataNascimento()).isEqualTo(proposta.getDataNascimento());
    }

    @Test
    @DisplayName("Retornar exceção ao filtrar por proposta, quando ele não existe na base")
    public void filtrarPropostaNaoEncontradoTest() {
        Integer id = 1;
        Mockito.when(propostaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NaoEncontradoException.class, () -> propostaService.filtrarProposta(id));

        Mockito.verify(propostaRepository, Mockito.times(1)).findById(id);
    }

    @Test
    @DisplayName("Retornar exceção ao filtrar por proposta, caso o passo 2 não tenha sido preenchido")
    public void filtrarPropostaValidarPasso2NaoPreenchidoTest() {
        Integer id = 1;
        Proposta proposta = this.criarProposta();
        Mockito.when(propostaRepository.findById(id)).thenReturn(Optional.of(proposta));
        Mockito.when(enderecoRepository.existsById(id)).thenReturn(false);

        assertThrows(EntidadeNaoProcessavelException.class, () -> propostaService.filtrarProposta(id));
    }

    @Test
    @DisplayName("Retornar exceção ao filtrar por proposta, caso o passo 3 não tenha sido preenchido")
    public void filtrarPropostaValidarPasso3NaoPreenchidoTest() {
        Integer id = 1;
        Proposta proposta = this.criarProposta();
        Mockito.when(propostaRepository.findById(id)).thenReturn(Optional.of(proposta));
        Mockito.when(enderecoRepository.existsById(id)).thenReturn(true);
        Mockito.when(arquivoRepository.existsByProposta(id)).thenReturn(false);

        assertThrows(EntidadeNaoProcessavelException.class, () -> propostaService.filtrarProposta(id));
    }

    @Test
    @DisplayName("Salvar proposta com sucesso")
    public void salvarTest() {
        Proposta proposta = this.criarProposta();
        Proposta entity = this.criarProposta();
        entity.setId(1);
        Mockito.when(propostaRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(propostaRepository.existsByCpf(Mockito.anyString())).thenReturn(false);
        Mockito.when(util.validarMaiorIdade(Mockito.anyInt(), Mockito.any(LocalDate.class))).thenReturn(true);
        Mockito.when(propostaRepository.save(proposta)).thenReturn(entity);

        Proposta propostaSalva = propostaService.salvar(proposta);

        assertThat(propostaSalva.getId()).isNotNull();
        assertThat(propostaSalva.getNome()).isEqualTo(proposta.getNome());
        assertThat(propostaSalva.getSobrenome()).isEqualTo(proposta.getSobrenome());
        assertThat(propostaSalva.getEmail()).isEqualTo(proposta.getEmail());
        assertThat(propostaSalva.getCpf()).isEqualTo(proposta.getCpf());
        assertThat(propostaSalva.getDataNascimento()).isEqualTo(proposta.getDataNascimento());
    }

    @Test
    @DisplayName("Retornar exceção ao salvar proposta, quando o email já foi cadastrado")
    public void salvarValidarEmailJaCadastradoTest() {
        Proposta proposta = this.criarProposta();
        Mockito.when(propostaRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        assertThrows(RegraNegocioException.class, () -> propostaService.salvar(proposta));
        Mockito.verify(propostaRepository, Mockito.never()).save(proposta);
    }

    @Test
    @DisplayName("Retornar exceção ao salvar proposta, quando o cpf já foi cadastrado")
    public void salvarValidarCpfJaCadastradoTest() {
        Proposta proposta = this.criarProposta();
        Mockito.when(propostaRepository.existsByCpf(Mockito.anyString())).thenReturn(true);

        assertThrows(RegraNegocioException.class, () -> propostaService.salvar(proposta));

        Mockito.verify(propostaRepository, Mockito.never()).save(proposta);
    }

    @Test
    @DisplayName("Retornar exceção ao salvar proposta, quando o cliente não é menor de idade")
    public void salvarValidarClienteMenorIdadeTest() {
        Proposta proposta = this.criarProposta();
        Mockito.when(util.validarMaiorIdade(Mockito.anyInt(), Mockito.any(LocalDate.class))).thenReturn(false);

        assertThrows(RegraNegocioException.class, () -> propostaService.salvar(proposta));

        Mockito.verify(propostaRepository, Mockito.never()).save(proposta);
    }

    @Test
    @DisplayName("Aceitar a proposta com sucesso")
    public void aceitarPropostaLiberadaTest() {
        Integer id = 1;
        Proposta proposta = this.criarProposta();
        proposta.setId(id);
        AceitePropostaDTO aceitePropostaDTO = AceitePropostaDTO.builder().aceite(true).build();
        Mockito.when(enderecoRepository.existsById(id)).thenReturn(true);
        Mockito.when(arquivoRepository.existsByProposta(id)).thenReturn(true);
        Mockito.when(propostaRepository.existsByStatus(Mockito.any(StatusProposta.class))).thenReturn(false);

        propostaService.aceiteProposta(proposta, aceitePropostaDTO);

        Mockito.verify(propostaRepository, Mockito.times(1)).save(proposta);
        Mockito.verify(contaService, Mockito.times(1)).criarConta(proposta);
    }

    @Test
    @DisplayName("Não aceitar a proposta")
    public void aceitarPropostaBloqueadaTest() {
        Integer id = 1;
        Proposta proposta = this.criarProposta();
        proposta.setId(id);
        AceitePropostaDTO aceitePropostaDTO = AceitePropostaDTO.builder().aceite(false).build();
        Mockito.when(enderecoRepository.existsById(id)).thenReturn(true);
        Mockito.when(arquivoRepository.existsByProposta(id)).thenReturn(true);
        Mockito.when(propostaRepository.existsByStatus(Mockito.any(StatusProposta.class))).thenReturn(false);

        propostaService.aceiteProposta(proposta, aceitePropostaDTO);

        Mockito.verify(propostaRepository, Mockito.times(1)).save(proposta);
        Mockito.verify(emailService, Mockito.times(1)).enviarEmail(null, null, proposta.getEmail());
    }

    @Test
    @DisplayName("Retornar exceção ao aceitar proposta, caso o passo 2 não tenha sido preenchido")
    public void aceitarPropostaValidarPasso2NaoPreenchidoTest() {
        Integer id = 1;
        Proposta proposta = this.criarProposta();
        proposta.setId(id);
        AceitePropostaDTO aceitePropostaDTO = AceitePropostaDTO.builder().aceite(true).build();
        Mockito.when(enderecoRepository.existsById(id)).thenReturn(false);

        assertThrows(EntidadeNaoProcessavelException.class, () -> propostaService.aceiteProposta(proposta, aceitePropostaDTO));

        Mockito.verify(propostaRepository, Mockito.never()).save(proposta);
        Mockito.verify(contaService, Mockito.never()).criarConta(proposta);
    }

    @Test
    @DisplayName("Retornar exceção ao aceitar proposta, caso o passo 3 não tenha sido preenchido")
    public void aceitarPropostaValidarPasso3NaoPreenchidoTest() {
        Integer id = 1;
        Proposta proposta = this.criarProposta();
        proposta.setId(id);
        AceitePropostaDTO aceitePropostaDTO = AceitePropostaDTO.builder().aceite(true).build();
        Mockito.when(arquivoRepository.existsByProposta(id)).thenReturn(false);

        assertThrows(EntidadeNaoProcessavelException.class, () -> propostaService.aceiteProposta(proposta, aceitePropostaDTO));

        Mockito.verify(propostaRepository, Mockito.never()).save(proposta);
        Mockito.verify(contaService, Mockito.never()).criarConta(proposta);
    }

    @Test
    @DisplayName("Retornar exceção ao aceitar proposta, caso a proposta já tenha sido aceita")
    public void aceitarPropostaValidarPropostaJaliberadaTest() {
        Integer id = 1;
        Proposta proposta = this.criarProposta();
        proposta.setId(id);
        AceitePropostaDTO aceitePropostaDTO = AceitePropostaDTO.builder().aceite(true).build();
        Mockito.when(propostaRepository.existsByStatus(Mockito.any(StatusProposta.class))).thenReturn(false);

        assertThrows(EntidadeNaoProcessavelException.class, () -> propostaService.aceiteProposta(proposta, aceitePropostaDTO));

        Mockito.verify(propostaRepository, Mockito.never()).save(proposta);
        Mockito.verify(contaService, Mockito.never()).criarConta(proposta);
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
