package com.bolao.v1.core.port.in.dto.response.grupo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrupoResponseDto {

    private Long id;
    private String codigo;
    private String nome;
    private Long campeonatoId;
}