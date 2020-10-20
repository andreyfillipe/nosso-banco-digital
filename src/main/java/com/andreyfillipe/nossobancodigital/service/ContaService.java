package com.andreyfillipe.nossobancodigital.service;

import com.andreyfillipe.nossobancodigital.entity.Conta;
import com.andreyfillipe.nossobancodigital.entity.Proposta;
import com.andreyfillipe.nossobancodigital.entity.Token;

public interface ContaService {

    Conta filtrarPorEmailECpf(String email, String cpf);

    Conta filtrarPorToken(int token);

    boolean existeAgenciaEContaECpf(String agencia, String conta, String cpf);

    Conta salvar(Conta conta, String email);

    void criarConta(Proposta proposta);

    Conta gerarConta(Proposta proposta);

    void criarSenha(Conta conta, Token token);

    void depositar(String agencia, String conta, String cpf, double valor);
}
