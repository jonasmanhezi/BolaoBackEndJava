package com.bolao.v1.infrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "palpite_campeao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PalpiteCampeaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "time_selecionado_id")
    private Long timeSelecionadoId;
}
