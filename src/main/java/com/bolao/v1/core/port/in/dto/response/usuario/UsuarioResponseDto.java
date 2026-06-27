package com.bolao.v1.core.port.in.dto.response.usuario;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDto extends RepresentationModel <UsuarioResponseDto>{


    private Long id;
    private String nome;
    private String email;
    private OffsetDateTime dataCriacao;


}
