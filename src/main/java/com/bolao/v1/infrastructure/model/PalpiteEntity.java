package com.bolao.v1.infrastructure.model;

import com.bolao.v1.core.domain.entity.palpite.Palpite;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tb_palpite")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PalpiteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "partida_id", nullable = false)
    private Integer partidaId;

    @Column(name = "usuario_id", nullable = false)
    private Integer usuarioId;

    @Column(name = "grupo_id")
    private Long grupoId;

    @Column(name = "gols_casa", nullable = false)
    private Integer golsCasa;

    @Column(name = "gols_visitante", nullable = false)
    private Integer golsVisitante;

    @Column(name = "pontuacao_obtida")
    private Integer pontuacaoObtida;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Palpite.StatusPalpite status;

}
