package com.andreyfillipe.nossobancodigital.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaItemDTO implements Serializable {

    @NotEmpty(message = "{campo.codigo-unico.obrigatorio}")
    private String codigoUnicoTransferencia;

    @NotEmpty(message = "{campo.documento-identificador.obrigatorio}")
    private String documentoIdentificadorOrigem;

    @NotEmpty(message = "{campo.banco-origem.obrigatorio}")
    private String bancoOrigem;

    @NotEmpty(message = "{campo.agencia-origem.obrigatorio}")
    private String agenciaOrigem;

    @NotEmpty(message = "{campo.conta-origem.obrigatorio}")
    private String contaOrigem;

    @NotNull(message = "{campo.data-transferencia.obrigatorio}")
    private LocalDate data;

    @NotNull(message = "{campo.valor-transferencia.obrigatorio}")
    private Double valor;

    @NotEmpty(message = "{campo.agencia-destino.obrigatorio}")
    private String agenciaDestino;

    @NotEmpty(message = "{campo.conta-destino.obrigatorio}")
    private String contaDestino;

    @NotEmpty(message = "{campo.cpf.obrigatorio}")
    @CPF(message = "{campo.cpf.invalido}")
    private String cpf;
}
