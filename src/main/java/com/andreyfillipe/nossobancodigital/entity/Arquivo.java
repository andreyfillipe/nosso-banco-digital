package com.andreyfillipe.nossobancodigital.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "arquivo")
public class Arquivo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "arquivo")
    private String arquivo;

    @ManyToOne
    @JoinColumn(name = "proposta_id")
    private Proposta proposta;
}
