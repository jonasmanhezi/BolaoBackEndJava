package com.bolao.v1.infrastructure.model;


import com.bolao.v1.core.domain.entity.partida.Partida;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_partida")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartidaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "campeonato_id", nullable = false)
    private Integer campeonatoId;

    @Column(name = "fase_id", nullable = false)
    private Integer faseId;

    @Column(name = "time_casa_id", nullable = false)
    private Integer timeCasaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_casa_id", insertable = false, updatable = false)
    private TimeEntity timeCasa;

    @Column(name = "time_visitante_id", nullable = false)
    private Integer timeVisitanteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_visitante_id", insertable = false, updatable = false)
    private TimeEntity timeVisitante;

    @Column(name= "data_hora_partida", nullable = false)
    private LocalDateTime dataHoraPartida;

    @Column(name = "gols_casa")
    private Integer golsCasa;

    @Column(name = "gols_visitante")
    private Integer golsVisitante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Partida.StatusPartida status;

    @Column(name = "external_id")
    private Integer externalId;

}
