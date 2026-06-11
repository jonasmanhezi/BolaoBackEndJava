package com.bolao.v1.shared.palpite;

import com.bolao.v1.core.domain.entity.partida.Partida;
import com.bolao.v1.shared.constant.Commons;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PalpiteDeadlinePolicyTest {

    private static final ZoneId BRAZIL = PalpiteDeadlinePolicy.BRAZIL_ZONE;

    @Test
    void aceitaPalpite_seteMinutosAntes_retornaTrue() {
        Instant kickoffUtc = kickoffBrasil(2026, 6, 11, 16, 0);
        Partida partida = partidaAgendada(kickoffUtc);
        ZonedDateTime agora = ZonedDateTime.of(2026, 6, 11, 15, 53, 0, 0, BRAZIL);

        assertTrue(PalpiteDeadlinePolicy.aceitaPalpite(partida, agora));
    }

    @Test
    void aceitaPalpite_exatamenteCincoMinutosAntes_retornaFalse() {
        Instant kickoffUtc = kickoffBrasil(2026, 6, 11, 16, 0);
        Partida partida = partidaAgendada(kickoffUtc);
        ZonedDateTime agora = ZonedDateTime.of(2026, 6, 11, 15, 55, 0, 0, BRAZIL);

        assertFalse(PalpiteDeadlinePolicy.aceitaPalpite(partida, agora));
    }

    @Test
    void aceitaPalpite_valorUtcDoBanco_19h14Brasil() {
        // tb_partida: 2026-05-19 22:14:51+00
        Instant kickoffUtc = Instant.parse("2026-05-19T22:14:51Z");
        Partida partida = partidaAgendada(kickoffUtc);

        ZonedDateTime antesDoPrazo = ZonedDateTime.of(2026, 5, 19, 19, 8, 59, 0, BRAZIL);
        ZonedDateTime depoisDoPrazo = ZonedDateTime.of(2026, 5, 19, 19, 10, 0, 0, BRAZIL);

        assertTrue(PalpiteDeadlinePolicy.aceitaPalpite(partida, antesDoPrazo));
        assertFalse(PalpiteDeadlinePolicy.aceitaPalpite(partida, depoisDoPrazo));
    }

    @Test
    void validarEnvioPermitido_aposDeadline_lancaExcecao() {
        Instant kickoffUtc = kickoffBrasil(2026, 6, 11, 16, 0);
        Partida partida = partidaAgendada(kickoffUtc);
        ZonedDateTime agora = ZonedDateTime.of(2026, 6, 11, 15, 59, 0, 0, BRAZIL);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> PalpiteDeadlinePolicy.validarEnvioPermitido(partida, agora)
        );
        assertTrue(ex.getMessage().contains(Commons.MSG_PALPITE_DEADLINE_CLOSED));
    }

    @Test
    void validarEnvioPermitido_partidaEmAndamento_lancaExcecaoDeJogoIniciado() {
        Partida partida = Partida.builder()
                .status(String.valueOf(Partida.StatusPartida.EM_ANDAMENTO))
                .dataHoraPartida(kickoffBrasil(2026, 6, 11, 16, 0))
                .build();
        ZonedDateTime agora = ZonedDateTime.of(2026, 6, 11, 14, 0, 0, 0, BRAZIL);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> PalpiteDeadlinePolicy.validarEnvioPermitido(partida, agora)
        );
        assertTrue(ex.getMessage().contains(Commons.MSG_GAME_ALREADY_STARTED));
    }

    @Test
    void validarEnvioPermitido_seisMinutosAntes_naoLanca() {
        Instant kickoffUtc = kickoffBrasil(2026, 6, 11, 16, 0);
        Partida partida = partidaAgendada(kickoffUtc);
        ZonedDateTime agora = ZonedDateTime.of(2026, 6, 11, 15, 54, 0, 0, BRAZIL);

        assertDoesNotThrow(() -> PalpiteDeadlinePolicy.validarEnvioPermitido(partida, agora));
    }

    private Instant kickoffBrasil(int year, int month, int day, int hour, int minute) {
        return ZonedDateTime.of(year, month, day, hour, minute, 0, 0, BRAZIL).toInstant();
    }

    private Partida partidaAgendada(Instant kickoffUtc) {
        return Partida.builder()
                .status(String.valueOf(Partida.StatusPartida.AGENDADA))
                .dataHoraPartida(kickoffUtc)
                .build();
    }
}