package com.bolao.v1.core.port.out.ranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingComNome {

    private Long userId;
    private Integer pontuacao;
    private String nome;
}