package com.bolao.v1.core.domain.entity.campeonato;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Campeonato {

    private Integer campeonatoId;
    private String nome;
    private String temporada;
    private Integer faseAtualId;


}
