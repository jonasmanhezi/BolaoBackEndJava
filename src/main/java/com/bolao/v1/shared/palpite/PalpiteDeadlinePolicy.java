package com.bolao.v1.shared.palpite;

import com.bolao.v1.core.domain.entity.partida.Partida;
import com.bolao.v1.shared.constant.Commons;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Enforces the business rule: palpites close {@value #MINUTOS_ANTES_KICKOFF} minutes
 * before kickoff. {@code data_hora_partida} is stored as UTC ({@code timestamptz}) and
 * converted to {@link #BRAZIL_ZONE} for the deadline check.
 */
public final class PalpiteDeadlinePolicy {

    public static final int MINUTOS_ANTES_KICKOFF = 5;
    public static final ZoneId BRAZIL_ZONE = ZoneId.of("America/Sao_Paulo");

    private PalpiteDeadlinePolicy() {
    }

    public static ZonedDateTime limiteEnvio(Instant kickoffUtc) {
        return kickoffUtc.atZone(BRAZIL_ZONE).minusMinutes(MINUTOS_ANTES_KICKOFF);
    }

    public static boolean aceitaPalpite(Partida partida) {
        return aceitaPalpite(partida, ZonedDateTime.now(BRAZIL_ZONE));
    }

    public static boolean aceitaPalpite(Partida partida, ZonedDateTime agoraBrasil) {
        if (!String.valueOf(Partida.StatusPartida.AGENDADA).equals(partida.getStatus())) {
            return false;
        }
        if (partida.getDataHoraPartida() == null) {
            return false;
        }
        return agoraBrasil.isBefore(limiteEnvio(partida.getDataHoraPartida()));
    }

    public static void validarEnvioPermitido(Partida partida) {
        validarEnvioPermitido(partida, ZonedDateTime.now(BRAZIL_ZONE));
    }

    public static void validarEnvioPermitido(Partida partida, ZonedDateTime agoraBrasil) {
        if (!String.valueOf(Partida.StatusPartida.AGENDADA).equals(partida.getStatus())) {
            throw new IllegalStateException(Commons.MSG_GAME_ALREADY_STARTED);
        }
        if (partida.getDataHoraPartida() == null) {
            throw new IllegalStateException("Horário da partida não está definido.");
        }
        if (!agoraBrasil.isBefore(limiteEnvio(partida.getDataHoraPartida()))) {
            throw new IllegalStateException(Commons.MSG_PALPITE_DEADLINE_CLOSED);
        }
    }
}