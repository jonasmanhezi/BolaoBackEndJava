package com.bolao.v1.core.port.in.dto.request.grupo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntrarGrupoRequestDto {

    @NotBlank(message = "Informe o código do grupo")
    private String codigo;
}