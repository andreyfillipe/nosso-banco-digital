package com.andreyfillipe.nossobancodigital.consumer;

import com.andreyfillipe.nossobancodigital.entity.Transferencia;
import com.andreyfillipe.nossobancodigital.entity.dto.TransferenciaItemDTO;
import com.andreyfillipe.nossobancodigital.service.ContaService;
import com.andreyfillipe.nossobancodigital.service.TransferenciaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransferenciaConsumer {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ObjectMapper objectMapper;
    private ModelMapper modelMapper;
    private TransferenciaService transferenciaService;
    private ContaService contaService;

    public TransferenciaConsumer(ObjectMapper objectMapper, ModelMapper modelMapper, TransferenciaService transferenciaService, ContaService contaService) {
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.transferenciaService = transferenciaService;
        this.contaService = contaService;
    }

    @Transactional
    @KafkaListener(topics = "transferencia", groupId = "transferencia-group-id")
    public void consume(String mensagem) {
        //Registrar a transferência e realizar depósito
        TransferenciaItemDTO dto = this.converterStringToObjeto(mensagem);
        if (this.contaService.existeAgenciaEContaECpf(dto.getAgenciaDestino(), dto.getContaDestino(), dto.getCpf())) {
            if (!this.transferenciaService.existeCodigoUnicoTransferencia(dto.getCodigoUnicoTransferencia())) {
                this.contaService.depositar(dto.getAgenciaDestino(), dto.getContaDestino(), dto.getCpf(), dto.getValor());
                Transferencia transferencia = this.modelMapper.map(dto, Transferencia.class);
                this.transferenciaService.salvar(transferencia);
            }
            else {
                logger.info("Trasferência já realizada para o código único: " + dto.getCodigoUnicoTransferencia());
            }
        }
    }

    private TransferenciaItemDTO converterStringToObjeto(String mensagem) {
        try {
            return this.objectMapper.readValue(mensagem, TransferenciaItemDTO.class);
        }
        catch (JsonProcessingException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
