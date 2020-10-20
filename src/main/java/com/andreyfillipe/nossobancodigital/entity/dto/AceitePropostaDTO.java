package com.andreyfillipe.nossobancodigital.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AceitePropostaDTO implements Serializable {

    @NotNull(message = "{campo.aceite.obrigatorio}")
    private Boolean aceite;
}
