package com.andreyfillipe.nossobancodigital.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropostaDTO implements Serializable {

    private Integer id;
    private String nome;
    private String sobrenome;
    private String email;
    private LocalDate dataNascimento;
    private String cpf;
    private EnderecoDTO endereco;
}
