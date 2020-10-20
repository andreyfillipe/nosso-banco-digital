package com.andreyfillipe.nossobancodigital.entity;

import com.andreyfillipe.nossobancodigital.entity.enums.StatusProposta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "proposta")
public class Proposta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome", length = 100)
    private String nome;

    @Column(name = "sobrenome", length = 100)
    private String sobrenome;

    @Column(name = "email")
    private String email;

    @Column(name = "cpf", length = 14)
    private String cpf;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusProposta status;

    @OneToOne(mappedBy = "proposta", cascade = CascadeType.ALL)
    private Endereco endereco;

    @OneToMany(mappedBy = "proposta")
    private List<Arquivo> arquivos;

    @OneToOne(mappedBy = "proposta", cascade = CascadeType.ALL)
    private Conta conta;
}
