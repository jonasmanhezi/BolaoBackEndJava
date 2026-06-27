package com.bolao.v1.infrastructure.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_usuario")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "supabase_user_id", unique = true)
    private UUID supabaseUserId;
}