package com.andreyfillipe.nossobancodigital.service.impl;

import com.andreyfillipe.nossobancodigital.entity.Proposta;
import com.andreyfillipe.nossobancodigital.entity.dto.AceitePropostaDTO;
import com.andreyfillipe.nossobancodigital.entity.dto.MensagemDTO;
import com.andreyfillipe.nossobancodigital.entity.enums.StatusProposta;
import com.andreyfillipe.nossobancodigital.repository.ArquivoRepository;
import com.andreyfillipe.nossobancodigital.repository.EnderecoRepository;
import com.andreyfillipe.nossobancodigital.repository.PropostaRepository;
import com.andreyfillipe.nossobancodigital.service.ContaService;
import com.andreyfillipe.nossobancodigital.service.EmailService;
import com.andreyfillipe.nossobancodigital.service.PropostaService;
import com.andreyfillipe.nossobancodigital.service.exception.EntidadeNaoProcessavelException;
import com.andreyfillipe.nossobancodigital.service.exception.NaoEncontradoException;
import com.andreyfillipe.nossobancodigital.service.exception.RegraNegocioException;
import com.andreyfillipe.nossobancodigital.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropostaServiceImpl implements PropostaService {

    @Value("${application.email-proposta-nao-aceita.assunto}")
    private String assunto;

    @Value("${application.email-proposta-nao-aceita.mensagem}")
    private String mensagem;

    @Value("${application.criacao-conta.mensagem}")
    private String mensagemContaCriada;

    private PropostaRepository propostaRepository;
    private EnderecoRepository enderecoRepository;
    private ArquivoRepository arquivoRepository;
    private ContaService contaService;
    private EmailService emailService;
    private Util util;

    public PropostaServiceImpl(PropostaRepository propostaRepository, EnderecoRepository enderecoRepository, ArquivoRepository arquivoRepository,
                               ContaService contaService, EmailService emailService, Util util) {
        this.propostaRepository = propostaRepository;
        this.enderecoRepository = enderecoRepository;
        this.arquivoRepository = arquivoRepository;
        this.contaService = contaService;
        this.emailService = emailService;
        this.util = util;
    }

    public Proposta filtrarPorId(int id) {
        //Filtrar proposta por id
        return this.propostaRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Código de proposta não encontrado"));
    }

    public Proposta filtrarProposta(int id) {
        //Filtrar proposta por id
        Proposta proposta = this.propostaRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Código de proposta não encontrado"));
        //Verificar se o passo 2 da proposta foi realizado
        if (!this.enderecoRepository.existsById(id)) {
            throw new EntidadeNaoProcessavelException("Por favor preencha o passo 2 da proposta");
        }
        //Verificar se o passo 3 da proposta foi realizado
        if (!this.arquivoRepository.existsByProposta(id)) {
            throw new EntidadeNaoProcessavelException("Por favor preencha o passo 3 da proposta");
        }
        return proposta;
    }

    public Proposta salvar(Proposta proposta) {
        //Verificar se o email já foi cadastrado
        if (this.propostaRepository.existsByEmail(proposta.getEmail())) {
            throw new RegraNegocioException("Email já cadastrado");
        }
        //Verificar se o cpf já foi cadastrado
        if (this.propostaRepository.existsByCpf(proposta.getCpf())) {
            throw new RegraNegocioException("Cpf já cadastrado");
        }
        //Validar se o cliente é maior de 18 anos
        if (!this.util.validarMaiorIdade(18, proposta.getDataNascimento())) {
            throw new RegraNegocioException("Cliente tem que ser maior de 18 anos");
        }
        //Salvar proposta
        return this.propostaRepository.save(proposta);
    }

    public MensagemDTO aceiteProposta(Proposta proposta, AceitePropostaDTO aceiteProposta) {
        //Verificar se o passo 2 da proposta foi realizado
        if (!this.enderecoRepository.existsById(proposta.getId())) {
            throw new EntidadeNaoProcessavelException("Por favor preencha o passo 2 da proposta");
        }
        //Verificar se o passo 2 da proposta foi realizado
        if (!this.arquivoRepository.existsByProposta(proposta.getId())) {
            throw new EntidadeNaoProcessavelException("Por favor preencha o passo 3 da proposta");
        }
        //Verificar se a proposta já foi liberada e existe uma conta pra ela
        if (this.propostaRepository.existsByStatus(StatusProposta.LIBERADA)) {
            throw new RegraNegocioException("Já existe uma conta para essa proposta");
        }
        //Atualizar status da proposta
        proposta.setStatus(aceiteProposta.getAceite() ? StatusProposta.LIBERADA : StatusProposta.BLOQUEADA);
        this.propostaRepository.save(proposta);
        //Verificar se a proposta foi aceita ou não
        if (aceiteProposta.getAceite()) {
            //Criar conta para proposta aceita
            this.contaService.criarConta(proposta);
        }
        else {
            //Enviar email implorando aceite
            this.emailService.enviarEmail(this.assunto, this.mensagem, proposta.getEmail());
        }
        //Retornar mensagem de aceite ou não
        return aceiteProposta.getAceite() ? new MensagemDTO(this.mensagemContaCriada) : new MensagemDTO("");
    }
}
