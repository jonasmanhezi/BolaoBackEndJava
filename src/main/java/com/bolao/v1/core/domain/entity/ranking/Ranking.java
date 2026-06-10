package com.bolao.v1.core.domain.entity.ranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ranking {

    private Long id;
    private Long userId;
    private Integer pontuacao;
    private OffsetDateTime createdAt;
}