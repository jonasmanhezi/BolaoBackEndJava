package com.bolao.v1.adapter.scheduler;

import com.bolao.v1.core.port.in.RankingPortIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RankingJob {

    private final RankingPortIn rankingService;

    @Scheduled(fixedDelay = 480000, initialDelay = 60000)
    public void atualizarRanking() {
        log.info("--- [JOB] Iniciando atualização do ranking ---");
        try {
            rankingService.atualizarRanking();
            log.info("--- [JOB] Atualização do ranking finalizada ---");
        } catch (Exception e) {
            log.error("[JOB] Erro ao atualizar ranking: {}", e.getMessage(), e);
        }
    }
}