package com.andreyfillipe.nossobancodigital.service.impl;

import com.andreyfillipe.nossobancodigital.entity.Endereco;
import com.andreyfillipe.nossobancodigital.repository.EnderecoRepository;
import com.andreyfillipe.nossobancodigital.service.PropostaEnderecoService;
import com.andreyfillipe.nossobancodigital.service.exception.RegraNegocioException;
import com.andreyfillipe.nossobancodigital.util.Util;
import org.springframework.stereotype.Service;

@Service
public class PropostaEnderecoServiceImpl implements PropostaEnderecoService {

    private EnderecoRepository enderecoRepository;
    private Util util;

    public PropostaEnderecoServiceImpl(EnderecoRepository enderecoRepository, Util util) {
        this.enderecoRepository = enderecoRepository;
        this.util = util;
    }

    public Endereco salvar(Endereco endereco) {
        //Verificar se o passo 2 da proposta já foi realizado
        if (this.enderecoRepository.existsById(endereco.getProposta().getId())) {
            throw new RegraNegocioException("Passo 2 já concluído prossiga para o passo 3");
        }
        //Validar se o cep é válido
        if (!this.util.validarCep(endereco.getCep())) {
            throw new RegraNegocioException("O campo Cep não está no formato adequado");
        }
        //Salvar endereço
        return this.enderecoRepository.save(endereco);
    }
}
