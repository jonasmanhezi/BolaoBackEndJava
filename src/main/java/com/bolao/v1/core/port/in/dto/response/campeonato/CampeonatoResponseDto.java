package com.bolao.v1.core.port.in.dto.response.campeonato;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampeonatoResponseDto extends RepresentationModel<CampeonatoResponseDto> {


    private Long id;
    private String nome;
    private String temporada;
    private Long faseAtualId;
}
