package com.andreyfillipe.nossobancodigital.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token")
public class Token implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "token")
    private Integer token;

    @Column(name = "expiracao")
    private LocalDateTime expiracao;

    @Column(name = "utilizado")
    private Boolean utilizado;

    @ManyToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;
}
