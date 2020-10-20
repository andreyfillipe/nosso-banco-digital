package com.andreyfillipe.nossobancodigital.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transferencia")
public class Transferencia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "codigo_unico_transferencia")
    private String codigoUnicoTransferencia;

    @Column(name = "documento_identificador")
    private String documentoIdentificadorOrigem;

    @Column(name = "banco", length = 3)
    private String bancoOrigem;

    @Column(name = "agencia", length = 4)
    private String agenciaOrigem;

    @Column(name = "conta", length = 8)
    private String contaOrigem;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "valor")
    private Double valor;
}
