package com.andreyfillipe.nossobancodigital.service;

import com.andreyfillipe.nossobancodigital.entity.Arquivo;
import org.springframework.web.multipart.MultipartFile;

public interface PropostaArquivoService {

    Arquivo salvar(Arquivo arquivo, MultipartFile arq);
}
