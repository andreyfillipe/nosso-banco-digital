package com.andreyfillipe.nossobancodigital.service;

import com.andreyfillipe.nossobancodigital.entity.Conta;
import com.andreyfillipe.nossobancodigital.entity.Token;

public interface TokenService {

    Token filtrarPorToken(int token);

    Token salvar(Token token, String email);

    Token gerarToken(Conta conta);
}
