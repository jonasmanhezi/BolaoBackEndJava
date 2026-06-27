package com.bolao.v1.infrastructure.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_time")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "logo")
    private String logo;
}