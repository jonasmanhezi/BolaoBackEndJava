package com.bolao.v1.core.port.in.dto.response.palpite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PalpiteResponseDto extends RepresentationModel<PalpiteResponseDto> {


    private Integer id;
    private Integer usuarioId;
    private Integer partidaId;
    private Integer golsCasa;
    private Integer golsVisitante;
    private Integer pontuacaoObtida;
    private LocalDateTime dataRegistro;


}
