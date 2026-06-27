package com.bolao.v1.core.domain.entity.palpitecampeao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PalpiteCampeao {

    private Long id;
    private OffsetDateTime createdAt;
    private Long usuarioId;
    private Long timeSelecionadoId;
}
