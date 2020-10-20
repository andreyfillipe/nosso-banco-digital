package com.andreyfillipe.nossobancodigital.service.impl;

import com.andreyfillipe.nossobancodigital.entity.Arquivo;
import com.andreyfillipe.nossobancodigital.repository.ArquivoRepository;
import com.andreyfillipe.nossobancodigital.repository.EnderecoRepository;
import com.andreyfillipe.nossobancodigital.service.PropostaArquivoService;
import com.andreyfillipe.nossobancodigital.service.exception.EntidadeNaoProcessavelException;
import com.andreyfillipe.nossobancodigital.service.exception.RegraNegocioException;
import com.andreyfillipe.nossobancodigital.util.SalvarArquivo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PropostaArquivoServiceImpl implements PropostaArquivoService {

    @Value("${arquivo.raiz}")
    private String raiz;

    @Value("${arquivo.diretorio}")
    private String diretorioArquivo;

    private ArquivoRepository arquivoRepository;
    private EnderecoRepository enderecoRepository;

    public PropostaArquivoServiceImpl(ArquivoRepository arquivoRepository, EnderecoRepository enderecoRepository) {
        this.arquivoRepository = arquivoRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public Arquivo salvar(Arquivo arquivo, MultipartFile arq) {
        //Verificar se o passo 2 da proposta foi realizado
        if (!this.enderecoRepository.existsById(arquivo.getProposta().getId())) {
            throw new EntidadeNaoProcessavelException("Por favor preencha o passo 2 da proposta");
        }
        //Validar se o arquivo existe
        if (arq.isEmpty()) {
            throw new RegraNegocioException("O campo Arquivo é obrigatório");
        }
        //Salvar arquivo no disco
        String caminho = SalvarArquivo.salvar(this.raiz, this.diretorioArquivo, arq);
        arquivo.setArquivo(caminho);
        //Salvar arquivo
        return this.arquivoRepository.save(arquivo);
    }
}
