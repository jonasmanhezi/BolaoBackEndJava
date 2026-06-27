package com.bolao.v1.core.domain.entity.partida;


import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Partida {

    private Integer id;
    private Integer campeonatoId;
    private Integer timeCasaId;
    private Integer timeVisitanteId;
    private Instant dataHoraPartida;

    private String nomeCasa;
    private String nomeVisitante;

    private String logoCasa;
    private String logoFora;
    private Integer golsCasa;
    private Integer golsVisitante;
    private String status;
    private Integer faseId;

    public enum StatusPartida {

        AGENDADA, EM_ANDAMENTO, FINALIZADA , CANCELADA
    }

    public void iniciarPartida() {
        if (String.valueOf(StatusPartida.FINALIZADA).equals(this.status)
                || String.valueOf(StatusPartida.CANCELADA).equals(this.status)) {
            throw new IllegalStateException("Você não pode iniciar uma partida finalizada ou cancelada.");
        }

        this.status = String.valueOf(StatusPartida.EM_ANDAMENTO);

        if (this.golsCasa == null) {
            this.golsCasa = 0;
        }
        if (this.golsVisitante == null) {
            this.golsVisitante = 0;
        }
    }

    public void atualizarPlacarAoVivo(Integer golsCasa, Integer golsVisitante) {
        if (String.valueOf(StatusPartida.FINALIZADA).equals(this.status)
                || String.valueOf(StatusPartida.CANCELADA).equals(this.status)) {
            return;
        }

        if (String.valueOf(StatusPartida.EM_ANDAMENTO).equals(this.status)) {
            if (this.golsCasa == null) {
                this.golsCasa = 0;
            }
            if (this.golsVisitante == null) {
                this.golsVisitante = 0;
            }
        }

        if (golsCasa != null) {
            this.golsCasa = golsCasa;
        }
        if (golsVisitante != null) {
            this.golsVisitante = golsVisitante;
        }
    }

    private Integer externalId;

    public void finalizarPartida(Integer golsCasa, Integer golsVisitante ) {

        if(this.status == String.valueOf(StatusPartida.FINALIZADA) || this.status == String.valueOf(StatusPartida.CANCELADA)) {
            throw new IllegalStateException("Você não pode finalizar uma partida que já foi finalizada ou cancelada");
        }
        if (golsVisitante == null || golsCasa == null) {
            throw new IllegalStateException("Você não pode finalizar a partida com o placa em branco.");

        }

        this.golsCasa = golsCasa;
        this.dataHoraPartida = dataHoraPartida;
        this.golsVisitante = golsVisitante;
        this.status = String.valueOf(StatusPartida.FINALIZADA);


    }

    public void cancelar() {

        if (this.status == String.valueOf(StatusPartida.FINALIZADA)) {
            throw new IllegalStateException("Não é possivel cancelar uma partida que já foi finalizada");
        }
        this.status = String.valueOf(StatusPartida.CANCELADA);
    }
}
