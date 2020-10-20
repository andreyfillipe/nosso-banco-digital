package com.andreyfillipe.nossobancodigital.service;

import com.andreyfillipe.nossobancodigital.entity.Transferencia;
import com.andreyfillipe.nossobancodigital.entity.dto.TransferenciaDTO;
import com.andreyfillipe.nossobancodigital.entity.dto.TransferenciaItemDTO;

public interface TransferenciaService {

    Transferencia salvar(Transferencia transferencia);

    boolean existeCodigoUnicoTransferencia(String codigo);

    void transferencia(TransferenciaDTO dto);

    String converterObjetoToString(TransferenciaItemDTO dto);
}
