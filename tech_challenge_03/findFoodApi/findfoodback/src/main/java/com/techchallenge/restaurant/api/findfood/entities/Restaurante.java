package com.techchallenge.restaurant.api.findfood.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_restaurante")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    private String localizacao;

    private String tipoCozinha;

    private String horarioFuncionamento;

    private int quantidadeTotalDeMesas;

}