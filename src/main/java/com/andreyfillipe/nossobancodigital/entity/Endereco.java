package com.andreyfillipe.nossobancodigital.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "endereco")
public class Endereco implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "cep", length = 9)
    private String cep;

    @Column(name = "rua", length = 100)
    private String rua;

    @Column(name = "bairro", length = 50)
    private String bairro;

    @Column(name = "complemento", length = 100)
    private String complemento;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado", length = 2)
    private String estado;

    @OneToOne
    @MapsId
    private Proposta proposta;
}
