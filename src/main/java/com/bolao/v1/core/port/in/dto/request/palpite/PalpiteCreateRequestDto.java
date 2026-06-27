package com.bolao.v1.core.port.in.dto.request.palpite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PalpiteCreateRequestDto {


    private Integer usuarioId;
    private Integer partidaId;
    private Integer campeonatoId;
    private Integer golsCasa;
    private Integer golsVisitante;
    private LocalDateTime dataRegistro;

}
