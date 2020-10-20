package com.andreyfillipe.nossobancodigital.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "conta")
public class Conta implements Serializable {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Setter
    @Column(name = "codigo_banco", length = 3)
    private String codigoBanco;

    @Setter
    @Column(name = "agencia", length = 4)
    private String agencia;

    @Setter
    @Column(name = "conta", length = 8)
    private String conta;

    @Column(name = "saldo", precision = 20, scale = 2)
    private Double saldo = 0.0;

    @Setter
    @Column(name = "senha")
    private String senha;

    @Setter
    @OneToOne
    @MapsId
    private Proposta proposta;

    @OneToMany(mappedBy = "conta")
    private List<Token> tokens;

    public void setDeposito(double valor) {
        this.saldo += valor;
    }
}
