package com.andreyfillipe.nossobancodigital.service.impl;

import com.andreyfillipe.nossobancodigital.entity.Conta;
import com.andreyfillipe.nossobancodigital.entity.Proposta;
import com.andreyfillipe.nossobancodigital.entity.Token;
import com.andreyfillipe.nossobancodigital.repository.ContaRepository;
import com.andreyfillipe.nossobancodigital.repository.TokenRepository;
import com.andreyfillipe.nossobancodigital.service.ContaService;
import com.andreyfillipe.nossobancodigital.service.EmailService;
import com.andreyfillipe.nossobancodigital.service.exception.NaoEncontradoException;
import com.andreyfillipe.nossobancodigital.service.exception.RegraNegocioException;
import com.andreyfillipe.nossobancodigital.util.Senha;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class ContaServiceImpl implements ContaService {

    @Value("${application.criacao-conta-email.assunto}")
    private String assunto;

    @Value("${application.criacao-conta-email.mensagem}")
    private String mensagem;

    @Value("${application.senha-alterada.assunto}")
    private String assuntoSenha;

    @Value("${application.senha-alterada.mensagem}")
    private String mensagemSenha;

    private ContaRepository contaRepository;
    private TokenRepository tokenRepository;
    private EmailService emailService;

    public ContaServiceImpl(ContaRepository contaRepository, TokenRepository tokenRepository, EmailService emailService) {
        this.contaRepository = contaRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    public Conta filtrarPorEmailECpf(String email, String cpf) {
        //Filtrar conta por email e cpf
        Conta conta = this.contaRepository.findByEmailAndCpf(email, cpf).orElseThrow(() -> new NaoEncontradoException("Conta não encontrada para o email e cpf informado"));
        return conta;
    }

    public Conta filtrarPorToken(int token) {
        //Filtrar conta por token
        Conta conta = this.contaRepository.findByToken(token).orElseThrow(() -> new NaoEncontradoException("Conta não encontrada para o token informado"));
        return conta;
    }

    public boolean existeAgenciaEContaECpf(String agencia, String conta, String cpf) {
        //Verificar se existe uma conta com os dados informado
        return this.contaRepository.existsByAgenciaAndContaAndCpf(agencia, conta, cpf);
    }

    public Conta salvar(Conta conta, String email) {
        //Salvar conta
        Conta entity = this.contaRepository.save(conta);
        //Enviar email dos dados da conta
        String mensagem = this.mensagem +
                String.format("\nDados da conta:\n") +
                String.format("Agência: %s\n", conta.getAgencia()) +
                String.format("Número da Conta: %s", conta.getConta());
        this.emailService.enviarEmail(this.assunto, mensagem, email);
        return entity;
    }

    @Async
    public void criarConta(Proposta proposta) {
        //Gerar número de conta
        Conta conta = this.gerarConta(proposta);
        //Salvar conta realizando 2 tentativas
        for (int i = 1; i <= 2; i++) {
            try {
                this.salvar(conta, proposta.getEmail());
                break;
            }
            catch (Exception ex) {
                try {
                    Thread.sleep(5000);
                }
                catch (IllegalArgumentException | InterruptedException e) {}
            }
        }
    }

    public Conta gerarConta(Proposta proposta) {
        //Gerar número de conta vinculando a proposta informada
        Random random = new Random();
        String codigoBanco = "159";
        String agencia = Integer.toString(random.nextInt(9999));
        String numeroConta = Integer.toString(random.nextInt(99999999));

        Conta conta = new Conta();
        conta.setAgencia(agencia);
        conta.setConta(numeroConta);
        conta.setCodigoBanco(codigoBanco);
        conta.setDeposito(0.0);
        conta.setProposta(proposta);
        return conta;
    }

    @Transactional
    public void criarSenha(Conta conta, Token token) {
        //Validar se senha forte
        List<String> senha = Senha.validarSenhaForte(conta.getSenha());
        if (senha != null) {
            throw new RegraNegocioException(senha.get(0));
        }
        //Validar se token está expirado
        if (this.tokenRepository.existsTokenExpirado(token.getToken(), LocalDateTime.now())) {
            throw new RegraNegocioException("Token expirado");
        }
        //Validar se token já foi utilizado
        if (this.tokenRepository.existsByTokenAndUtilizado(token.getToken(), true)) {
            throw new RegraNegocioException("Token já utilizado");
        }
        //Gerar senha hash e atualizar senha da conta
        conta.setSenha(Senha.gerarSenhaHash(conta.getSenha()));
        this.contaRepository.save(conta);
        //Atualizar token para utilizado
        token.setUtilizado(true);
        this.tokenRepository.save(token);
        //Enviar email de senha alterada com sucesso
        this.emailService.enviarEmail(this.assuntoSenha, this.mensagemSenha, conta.getProposta().getEmail());
    }

    public void depositar(String agencia, String conta, String cpf, double valor) {
        //Realizar depósito na conta informada
        Conta entity = this.contaRepository.findByAgenciaAndContaAndCpf(agencia, conta, cpf).orElse(null);
        if (entity != null) {
            entity.setDeposito(valor);
            this.contaRepository.save(entity);
        }
    }
}
