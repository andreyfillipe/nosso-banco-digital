package com.andreyfillipe.nossobancodigital.service.impl;

import com.andreyfillipe.nossobancodigital.entity.Transferencia;
import com.andreyfillipe.nossobancodigital.entity.dto.TransferenciaDTO;
import com.andreyfillipe.nossobancodigital.entity.dto.TransferenciaItemDTO;
import com.andreyfillipe.nossobancodigital.repository.TransferenciaRepository;
import com.andreyfillipe.nossobancodigital.service.TransferenciaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TransferenciaServiceImpl implements TransferenciaService {

    private ObjectMapper objectMapper;
    private KafkaTemplate<String, String> kafkaTemplate;
    private TransferenciaRepository transferenciaRepository;

    public TransferenciaServiceImpl(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate, TransferenciaRepository transferenciaRepository) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.transferenciaRepository = transferenciaRepository;
    }

    public Transferencia salvar(Transferencia transferencia) {
        //Salvar transferência
        return this.transferenciaRepository.save(transferencia);
    }

    public boolean existeCodigoUnicoTransferencia(String codigo) {
        //Verificar se a transferência já foi processada
        return this.transferenciaRepository.existsByCodigoUnicoTransferencia(codigo);
    }

    @Async
    public void transferencia(TransferenciaDTO dto) {
        //Enviar dados da transferência para o Kafka, para que seja processado as transferências
        for(TransferenciaItemDTO item: dto.getTransferencias()) {
            String mensagem = this.converterObjetoToString(item);
            this.kafkaTemplate.send("transferencia", mensagem);
        }
    }

    public String converterObjetoToString(TransferenciaItemDTO dto) {
        //Converter transferência em json
        try {
            return this.objectMapper.writeValueAsString(dto);
        }
        catch (JsonProcessingException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
