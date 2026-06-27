package com.bolao.v1.core.domain.entity.usuario;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private Long id;
    private String email;
    private String nome;
    private LocalDateTime dataCriacao;
    private UUID supabaseUserId;
}