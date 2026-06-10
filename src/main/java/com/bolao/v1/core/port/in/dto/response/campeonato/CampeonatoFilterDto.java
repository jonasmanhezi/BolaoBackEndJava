package com.bolao.v1.core.port.in.dto.response.campeonato;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampeonatoFilterDto {

    private String nome;
    private String temporada;
    private Long faseAtualId;
}
