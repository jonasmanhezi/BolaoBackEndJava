package com.bolao.v1.adapter.scheduler;

import com.bolao.v1.core.port.in.PartidaPortIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SincronizacaoPartidasJob {

    private final PartidaPortIn partidaService;

    @Scheduled(fixedDelay = 300000)
    public void executarSincronizacaoGols() {
        log.info("--- [JOB REST] Iniciando sincronização de gols com a API externa ---");

        partidaService.sincronizarPartidasDoDia();


        log.info("--- [JOB REST] Sincronização de gols finalizada ---");
    }
}
