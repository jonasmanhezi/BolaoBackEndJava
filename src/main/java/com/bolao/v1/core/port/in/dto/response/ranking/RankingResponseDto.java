package com.bolao.v1.core.port.in.dto.response.ranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingResponseDto {

    private Integer posicao;
    private Long userId;
    private String nome;
    private Integer pontuacao;
}