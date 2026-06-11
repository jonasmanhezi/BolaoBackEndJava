package com.bolao.v1.core.domain.entity.grupo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grupo {

    private Long id;
    private String codigo;
    private String nome;
    private Long campeonatoId;
}