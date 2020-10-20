package com.andreyfillipe.nossobancodigital.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadastroSenhaDTO implements Serializable {

    @NotNull(message = "{campo.token.obrigatorio}")
    private Integer token;
    @NotEmpty(message = "{campo.senha.obrigatorio}")
    private String senha;
}
