package com.bolao.v1.infrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "tb_grupo_membro")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrupoMembroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "grupo_id")
    private Long grupoId;

    @Column(name = "usuario_id")
    private Long usuarioId;
}