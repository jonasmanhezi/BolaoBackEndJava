package com.bolao.v1.infrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_campeonato")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampeonatoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer campeonatoId;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "temporada", length = 20, nullable = false)
    private String temporada;

    @Column(name = "fase_atual_id")
    private Integer faseAtualId;


}