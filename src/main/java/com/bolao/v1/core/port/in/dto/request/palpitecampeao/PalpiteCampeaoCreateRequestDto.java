package com.bolao.v1.core.port.in.dto.request.palpitecampeao;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PalpiteCampeaoCreateRequestDto {

    @NotNull(message = "O time selecionado é obrigatório")
    private Long timeSelecionadoId;
}
