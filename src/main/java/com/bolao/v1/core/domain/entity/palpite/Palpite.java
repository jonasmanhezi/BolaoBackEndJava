package com.bolao.v1.core.domain.entity.palpite;

import com.bolao.v1.core.domain.entity.partida.Partida;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Palpite {

    private Integer id;
    private Integer usuarioId;
    private Long grupoId;
    private Integer partidaId;
    private Integer golsCasa;
    private Integer golsVisitante;
    private Integer pontuacaoObtida;
    private Integer palpiteWinnerId;
    private Integer faseId;
    private LocalDateTime dataRegistro;

    private StatusPalpite status = StatusPalpite.PENDENTE;

    public enum StatusPalpite {
        PENDENTE, PONTUADO
    }



    public boolean pertenceAoUsuario(Integer idDoUsuarioLogado) {
        return this.usuarioId.equals(idDoUsuarioLogado);
    }

    public void pontuarPalpite(Integer resultadoGolsCasa, Integer resultadoGolsVisitante, Integer faseId) {
        pontuarPalpite(resultadoGolsCasa, resultadoGolsVisitante, faseId, false, null);
    }

    public void pontuarPalpite(Integer resultadoGolsCasa, Integer resultadoGolsVisitante, Integer faseId,
                                boolean temPenalti, Integer winnerId) {
        if (this.status == StatusPalpite.PONTUADO) {
            return;
        }

        int pontosExato;
        int pontosTendencia;

        if (faseId != null && faseId > 1) {
            pontosExato = 50;
            pontosTendencia = 20;
        } else {
            pontosExato = 25;
            pontosTendencia = 10;
        }

        boolean acertouPlacarExato = this.golsCasa.equals(resultadoGolsCasa)
                && this.golsVisitante.equals(resultadoGolsVisitante);

        if (temPenalti) {
            boolean acertouEmpate = Integer.compare(this.golsCasa, this.golsVisitante) == 0
                    && Integer.compare(resultadoGolsCasa, resultadoGolsVisitante) == 0;
            boolean acertouWinner = winnerId != null && winnerId.equals(this.palpiteWinnerId);

            int pontos = 0;
            if (acertouPlacarExato) pontos += pontosExato;
            else if (acertouEmpate)  pontos += pontosTendencia;
            if (acertouWinner)       pontos += pontosTendencia;

            this.pontuacaoObtida = pontos;
            this.status = StatusPalpite.PONTUADO;
            return;
        }

        if (acertouPlacarExato) {
            this.pontuacaoObtida = pontosExato;
            this.status = StatusPalpite.PONTUADO;
            return;
        }

        Integer tendenciaPalpite = Integer.compare(this.golsCasa, this.golsVisitante);
        Integer tendenciaResultado = Integer.compare(resultadoGolsCasa, resultadoGolsVisitante);

        if (tendenciaPalpite.equals(tendenciaResultado)) {
            this.pontuacaoObtida = pontosTendencia;
            this.status = StatusPalpite.PONTUADO;
            return;
        }

        this.pontuacaoObtida = 0;
        this.status = StatusPalpite.PONTUADO;
    }

    public void atualizarPalpite(Integer novosGolsCasa, Integer novosGolsVisitante, Integer novoPalpiteWinnerId, Partida.StatusPartida statusPartida) {

        if(statusPartida != Partida.StatusPartida.AGENDADA) {
            throw new IllegalStateException("Você não pode atualizar o palpite enquanto o jogo está em andamento ou finalizado.");
        }
        this.golsCasa = novosGolsCasa;
        this.golsVisitante = novosGolsVisitante;
        this.palpiteWinnerId = novoPalpiteWinnerId;





    }



    }


