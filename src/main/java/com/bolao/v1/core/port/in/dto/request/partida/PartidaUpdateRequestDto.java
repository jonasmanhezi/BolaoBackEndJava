package com.bolao.v1.core.port.in.dto.request.partida;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartidaUpdateRequestDto {



    private Long id;
    private Long campeonatoId;
    private String timeCasa;
    private String timeVisitante;
    private OffsetDateTime dataHoraPartida;
    private Integer golsCasa;
    private Integer golsVisitante;
    private String status;
    private Long faseId;
}
