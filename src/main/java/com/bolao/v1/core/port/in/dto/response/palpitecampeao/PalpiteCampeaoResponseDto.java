package com.bolao.v1.core.port.in.dto.response.palpitecampeao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PalpiteCampeaoResponseDto {

    private Long id;
    private Long usuarioId;
    private Long timeSelecionadoId;
    private OffsetDateTime createdAt;
}
