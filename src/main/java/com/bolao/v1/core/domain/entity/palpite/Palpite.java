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
    private Integer partidaId;
    private Integer golsCasa;
    private Integer golsVisitante;
    private Integer pontuacaoObtida;
    private LocalDateTime dataRegistro;

    private StatusPalpite status = StatusPalpite.PENDENTE;

    public enum StatusPalpite {
        PENDENTE, PONTUADO
    }



    public boolean pertenceAoUsuario(Integer idDoUsuarioLogado) {
        return this.usuarioId.equals(idDoUsuarioLogado);
    }

    public void pontuarPalpite(Integer resultadoGolsCasa, Integer resultadoGolsVisitante) {

        if (this.status == StatusPalpite.PONTUADO) {
            return;
        }

        if(this.golsCasa .equals(resultadoGolsCasa) && this.golsVisitante.equals(resultadoGolsVisitante)){
                this.pontuacaoObtida = 25;
                this.status = StatusPalpite.PONTUADO;
                return;
        }

        Integer tendenciaPalpite = Integer.compare(this.golsCasa, this.golsVisitante);
        Integer tendenciaResultado = Integer.compare(resultadoGolsCasa, resultadoGolsVisitante);


        if (tendenciaPalpite == tendenciaResultado) {
            this.pontuacaoObtida = 10;
            this.status = StatusPalpite.PONTUADO;
            return;
        }

        this.pontuacaoObtida = 0;
        this.status = StatusPalpite.PONTUADO;
    }

    public void atualizarPalpite(Integer novosGolsCasa, Integer novosGolsVisitante,Partida.StatusPartida statusPartida) {

        if(statusPartida != Partida.StatusPartida.AGENDADA) {
            throw new IllegalStateException("Você não pode atualizar o palpite enquanto o jogo está em andamento ou finalizado.");

        }
        this.golsCasa = novosGolsCasa;
        this.golsVisitante = novosGolsVisitante;





    }



    }


