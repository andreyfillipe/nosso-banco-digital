package com.andreyfillipe.nossobancodigital.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaDTO implements Serializable {

    @NotEmpty(message = "{campo.dados-transferencia.obrigatorio}")
    private List<@Valid TransferenciaItemDTO> transferencias;
}
