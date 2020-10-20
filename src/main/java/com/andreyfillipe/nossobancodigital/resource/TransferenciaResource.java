package com.andreyfillipe.nossobancodigital.resource;

import com.andreyfillipe.nossobancodigital.entity.dto.TransferenciaDTO;
import com.andreyfillipe.nossobancodigital.service.TransferenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaResource {

    private TransferenciaService transferenciaService;

    public TransferenciaResource(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @PostMapping
    public ResponseEntity<Void> transferencia(@RequestBody @Valid TransferenciaDTO dto) {
        //Realizar transferência
        this.transferenciaService.transferencia(dto);
        return ResponseEntity.ok().build();
    }
}
