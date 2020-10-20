package com.andreyfillipe.nossobancodigital.service;

import com.andreyfillipe.nossobancodigital.entity.Proposta;
import com.andreyfillipe.nossobancodigital.entity.dto.AceitePropostaDTO;
import com.andreyfillipe.nossobancodigital.entity.dto.MensagemDTO;

public interface PropostaService {

    Proposta filtrarPorId(int id);

    Proposta filtrarProposta(int id);

    Proposta salvar(Proposta proposta);

    MensagemDTO aceiteProposta(Proposta proposta, AceitePropostaDTO aceiteProposta);


}
