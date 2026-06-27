package com.bolao.v1.core.port.in.dto.request.campeonato;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampeonatoUpdateRequestDto {

    private Integer id;
    private String nome;
    private String temporada;
    private Long faseAtualId;
}
