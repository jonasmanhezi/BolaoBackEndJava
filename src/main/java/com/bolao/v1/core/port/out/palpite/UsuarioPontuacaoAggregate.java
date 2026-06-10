package com.bolao.v1.core.port.out.palpite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPontuacaoAggregate {

    private Long usuarioId;
    private Integer pontuacaoTotal;
}