package com.bolao.v1.adapter.scheduler;


import com.bolao.v1.core.domain.entity.partida.Partida;
import com.bolao.v1.core.port.in.PalpitePortIn;
import com.bolao.v1.core.port.in.RankingPortIn;
import com.bolao.v1.core.port.out.partida.PartidaRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PontuacaoPalpitesJob {


    private final PalpitePortIn palpiteService;
    private final RankingPortIn rankingService;
    private final PartidaRepositoryPortOut partidaRepositoryPortOut;

    @Scheduled(fixedDelay = 300000)
    public void executarMotorDePontuacao() {

        log.info("--- [JOB] Iniciando varredura de partidas finalizadas para pontuar ---");

        List<Partida> partidasFinalizadas = partidaRepositoryPortOut.findByStatus(Partida.StatusPartida.FINALIZADA);

        if (partidasFinalizadas.isEmpty()) {
            log.info("[JOB] Nenhuma partida finalizada pendente de processamento.");
            return;
        }

        for (Partida partida : partidasFinalizadas) {
            try {
                log.info("[JOB] Processando pontos para a partida ID: {}", partida.getId());

                palpiteService.pontuarPalpite(
                        partida.getId(),
                        partida.getGolsCasa(),
                        partida.getGolsVisitante(),
                        partida.getFaseId()
                );

            } catch (Exception e) {
                log.error("[JOB] Erro crítico ao processar partida ID: {}. Motivo: {}", partida.getId(), e.getMessage());
            }
        }

        try {
            rankingService.atualizarRanking();
        } catch (Exception e) {
            log.error("[JOB] Erro ao atualizar ranking após pontuação: {}", e.getMessage(), e);
        }

        log.info("--- [JOB] Varredura de pontuação finalizada ---");
    }
}


