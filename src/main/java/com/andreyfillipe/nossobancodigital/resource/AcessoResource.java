package com.andreyfillipe.nossobancodigital.resource;

import com.andreyfillipe.nossobancodigital.entity.Conta;
import com.andreyfillipe.nossobancodigital.entity.Token;
import com.andreyfillipe.nossobancodigital.entity.dto.AcessoDTO;
import com.andreyfillipe.nossobancodigital.entity.dto.CadastroSenhaDTO;
import com.andreyfillipe.nossobancodigital.service.ContaService;
import com.andreyfillipe.nossobancodigital.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/acessos")
public class AcessoResource {

    private TokenService tokenService;
    private ContaService contaService;

    public AcessoResource(TokenService tokenService, ContaService contaService) {
        this.tokenService = tokenService;
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<Void> gerarToken(@RequestBody @Valid AcessoDTO dto) {
        //Filtrar conta por email e cpf
        Conta conta = this.contaService.filtrarPorEmailECpf(dto.getEmail(), dto.getCpf());
        //Gerar token para a conta filtrada
        Token token = this.tokenService.gerarToken(conta);
        //Salvar token e enviar email
        this.tokenService.salvar(token, dto.getEmail());
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<Void> cadastrarSenha(@RequestBody @Valid CadastroSenhaDTO dto) {
        //Filtrar token por token
        Token token = this.tokenService.filtrarPorToken(dto.getToken());
        //Filtrar conta por token
        Conta conta = this.contaService.filtrarPorToken(token.getToken());
        conta.setSenha(dto.getSenha());
        //Criar nova senha da conta
        this.contaService.criarSenha(conta, token);
        return ResponseEntity.ok().build();
    }
}
