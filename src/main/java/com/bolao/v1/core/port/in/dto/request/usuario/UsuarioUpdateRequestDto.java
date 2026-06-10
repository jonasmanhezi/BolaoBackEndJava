package com.bolao.v1.core.port.in.dto.request.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioUpdateRequestDto {


    private Long id;
    @NotBlank(message = "O nome é obrigatório")
    private String nome;


}
