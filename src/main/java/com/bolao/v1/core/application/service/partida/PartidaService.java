package com.bolao.v1.core.application.service.partida;


import com.bolao.v1.core.domain.entity.partida.Partida;
import com.bolao.v1.core.port.in.PartidaPortIn;
import com.bolao.v1.core.port.in.dto.request.partida.PartidaUpdateFinalizarDto;
import com.bolao.v1.core.port.in.dto.request.partida.PartidaUpdateRequestDto;
import com.bolao.v1.core.port.in.dto.response.partida.PartidaResponseDto;
import com.bolao.v1.core.port.in.dto.response.partidaExterna.PartidaExternaDto;
import com.bolao.v1.core.port.out.partida.PartidaRepositoryPortOut;
import com.bolao.v1.core.port.out.partidaExterna.PartidaExternaPortOut;
import com.bolao.v1.shared.fixture.FixtureApiStatusMapper;
import com.bolao.v1.shared.fixture.FixtureApiStatusMapper.Category;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartidaService implements PartidaPortIn {


    private final PartidaRepositoryPortOut partidaPortOut;
    private final ModelMapper modelMapper;
    private final PartidaExternaPortOut partidaExternaPortOut;

    @Override
    public List<PartidaResponseDto> findAll() {
        List<Partida> partidas = partidaPortOut.findAll();

        return partidas.stream()
                .map(partida -> modelMapper.map(partida, PartidaResponseDto.class))
                .toList();
    }


    @Override
    public PartidaResponseDto findById(Integer id) {
        Partida partida = partidaPortOut.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Partida não encontrada"));

        return modelMapper.map(partida, PartidaResponseDto.class);
    }

    @Override
    @Transactional
    public void iniciarPartida(Integer id) {
        Partida partida = partidaPortOut.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Partida não encontrada"));

        partida.iniciarPartida();

        partidaPortOut.save(partida);
    }
    @Override
    @Transactional
    public void finalizarPartida(Integer id, PartidaUpdateFinalizarDto request) {
        Partida partida = partidaPortOut.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Partida não encontrada"));

        partida.finalizarPartida(request.getGolsCasa(), request.getGolsVisitante());

        partidaPortOut.save(partida);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        if (!partidaPortOut.existsById(id)) {
            throw new EntityNotFoundException("Partida não encontrada: " + id);
        }
        partidaPortOut.deleteById(id);
    }

    @Override
    public List<PartidaResponseDto> findByFaseId(Integer faseId, Integer campeonatoId) {
        List<Partida> partidas = partidaPortOut.findByFaseId(faseId, campeonatoId);
        return partidas.stream()
                .map(partida -> modelMapper.map(partida, PartidaResponseDto.class))
                .toList();
    }
    @Override
    @Transactional
    public void sincronizarPartidasDoDia() {
        ZoneId brazil = ZoneId.of("America/Sao_Paulo");
        ZonedDateTime agoraBrasil = ZonedDateTime.now(brazil);
        Instant inicioDoDia = agoraBrasil.minusDays(1).toInstant();
        Instant fimDoDia = agoraBrasil.plusDays(1).toInstant();

        List<Partida> partidasDeHoje = partidaPortOut.findPartidasDeHoje(inicioDoDia, fimDoDia);

        List<Partida> partidasParaAtualizar = partidasDeHoje.stream()
                .filter(p -> !String.valueOf(Partida.StatusPartida.FINALIZADA).equals(p.getStatus()))
                .filter(p -> !String.valueOf(Partida.StatusPartida.CANCELADA).equals(p.getStatus()))
                .toList();

        if (partidasParaAtualizar.isEmpty()) {
            log.info("Nenhuma partida pendente de atualização para o dia de hoje.");
            return;
        }

        for (Partida partida : partidasParaAtualizar) {
            if (partida.getExternalId() == null) {
                continue;
            }

            try {
                PartidaExternaDto dadosExternos = partidaExternaPortOut
                        .buscarDadosPartidaExterna(partida.getExternalId());

                Category apiCategory = FixtureApiStatusMapper.categorize(dadosExternos.getStatusShort());

                log.info(
                        "Partida Local ID {}: Status Local='{}' | API='{}' | Categoria='{}'",
                        partida.getId(),
                        partida.getStatus(),
                        dadosExternos.getStatusShort(),
                        apiCategory
                );

                aplicarDadosExternos(partida, dadosExternos, apiCategory);

            } catch (Exception e) {
                log.error("Falha ao sincronizar dados externos da partida local ID: {}. Erro: {}", partida.getId(), e.getMessage());
            }
        }
    }

    private void aplicarDadosExternos(Partida partida, PartidaExternaDto dadosExternos, Category apiCategory) {
        Integer placarCasa = dadosExternos.getPlacarCasa();
        Integer placarVisitante = dadosExternos.getPlacarVisitante();

        switch (apiCategory) {
            case IN_PLAY -> {
                if (partida.getStatus() == null
                        || String.valueOf(Partida.StatusPartida.AGENDADA).equals(partida.getStatus())) {
                    partida.iniciarPartida();
                    log.info("Partida ID {} mudou para EM_ANDAMENTO (API: {}).", partida.getId(), dadosExternos.getStatusShort());
                }
                partida.atualizarPlacarAoVivo(placarCasa, placarVisitante);
                partidaPortOut.save(partida);
            }
            case FINISHED -> {
                Integer golsCasa = placarCasa != null ? placarCasa : 0;
                Integer golsVisitante = placarVisitante != null ? placarVisitante : 0;
                if (placarCasa == null || placarVisitante == null) {
                    log.warn(
                            "Partida ID {} finalizada pela API ({}) sem placar completo; usando {}x{}.",
                            partida.getId(), dadosExternos.getStatusShort(), golsCasa, golsVisitante
                    );
                }
                partida.finalizarPartida(golsCasa, golsVisitante);
                partidaPortOut.save(partida);
                log.info("Partida ID {} finalizada com placar: {}x{} (API: {}).",
                        partida.getId(), golsCasa, golsVisitante, dadosExternos.getStatusShort());
            }
            case CANCELLED -> {
                partida.cancelar();
                partidaPortOut.save(partida);
                log.info("Partida ID {} cancelada (API: {}).", partida.getId(), dadosExternos.getStatusShort());
            }
            case POSTPONED -> log.info(
                    "Partida ID {} adiada pela API ({}); mantendo status local '{}'.",
                    partida.getId(), dadosExternos.getStatusShort(), partida.getStatus()
            );
            case SCHEDULED -> {
                if (placarCasa != null || placarVisitante != null) {
                    if (partida.getStatus() == null
                            || String.valueOf(Partida.StatusPartida.AGENDADA).equals(partida.getStatus())) {
                        partida.iniciarPartida();
                        log.info(
                                "Partida ID {} inferida EM_ANDAMENTO (API '{}' com placar publicado).",
                                partida.getId(), dadosExternos.getStatusShort()
                        );
                    }
                    partida.atualizarPlacarAoVivo(placarCasa, placarVisitante);
                    partidaPortOut.save(partida);
                }
            }
            case UNKNOWN -> log.warn(
                    "Partida ID {} com status API não mapeado: '{}'. Nenhuma alteração aplicada.",
                    partida.getId(), dadosExternos.getStatusShort()
            );
        }
    }

}
