package com.andreyfillipe.nossobancodigital.service.impl;

import com.andreyfillipe.nossobancodigital.entity.Conta;
import com.andreyfillipe.nossobancodigital.entity.Token;
import com.andreyfillipe.nossobancodigital.repository.TokenRepository;
import com.andreyfillipe.nossobancodigital.service.EmailService;
import com.andreyfillipe.nossobancodigital.service.TokenService;
import com.andreyfillipe.nossobancodigital.service.exception.NaoEncontradoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${application.token.assunto}")
    private String assunto;

    @Value("${application.token.mensagem}")
    private String mensagem;

    @Value("${application.token.expiracao}")
    private int expiracao;

    private TokenRepository tokenRepository;
    private EmailService emailService;

    public TokenServiceImpl(TokenRepository tokenRepository, EmailService emailService) {
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    public Token filtrarPorToken(int token) {
        //Filtrar por token
        return this.tokenRepository.findOneByAndTokenOrderByIdDesc(token).orElseThrow(() -> new NaoEncontradoException("Token inválido"));
    }

    public Token salvar(Token token, String email) {
        //Salvar token
        Token entity = this.tokenRepository.save(token);
        //Enviar email com os dados do token
        this.emailService.enviarEmail(this.assunto, this.mensagem + token.getToken(), email);
        return entity;
    }

    public Token gerarToken(Conta conta) {
        //Gerar um número de token vinculado a conta informada
        Random random = new Random();
        int numeroToken = random.nextInt(999999);
        LocalDateTime dataExpiracao = LocalDateTime.now().plusMinutes(this.expiracao);
        Token token = new Token();
        token.setToken(numeroToken);
        token.setExpiracao(dataExpiracao);
        token.setUtilizado(false);
        token.setConta(conta);
        return token;
    }
}
