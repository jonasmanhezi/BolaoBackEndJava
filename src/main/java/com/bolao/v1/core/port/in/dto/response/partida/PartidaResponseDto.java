package com.bolao.v1.core.port.in.dto.response.partida;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartidaResponseDto extends RepresentationModel<PartidaResponseDto> {

    private Integer id;
    private Integer campeonatoId;
    private Integer timeCasaId;
    private Integer timeVisitanteId;
    private LocalDateTime dataHoraPartida;
    private Integer golsCasa;
    private Integer golsVisitante;
    private String status;
    private Integer faseId;

    private String nomeCasa;
    private String nomeVisitante;

    private String logoCasa;
    private String logoFora;


}
