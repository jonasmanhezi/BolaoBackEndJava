package com.bolao.v1.core.application.service.partida;


import com.bolao.v1.core.domain.entity.partida.Partida;
import com.bolao.v1.core.port.in.PartidaPortIn;
import com.bolao.v1.core.port.in.dto.request.partida.PartidaUpdateFinalizarDto;
import com.bolao.v1.core.port.in.dto.request.partida.PartidaUpdateRequestDto;
import com.bolao.v1.core.port.in.dto.response.partida.PartidaResponseDto;
import com.bolao.v1.core.port.in.dto.response.partidaExterna.PartidaExternaDto;
import com.bolao.v1.core.port.out.partida.PartidaRepositoryPortOut;
import com.bolao.v1.core.port.out.partidaExterna.PartidaExternaPortOut;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        LocalDateTime inicioDoDia = LocalDateTime.now().minusDays(1);
        LocalDateTime fimDoDia = LocalDateTime.now().plusDays(1);

        List<Partida> partidasDeHoje = partidaPortOut.findPartidasDeHoje(inicioDoDia, fimDoDia);

        List<Partida> partidasParaAtualizar = partidasDeHoje.stream()
                .filter(p -> !String.valueOf(Partida.StatusPartida.FINALIZADA).equals(p.getStatus()))
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

                log.info("Partida Local ID {}: Status Local='{}' | Status API='{}'",
                        partida.getId(), partida.getStatus(), dadosExternos.getStatus());

                if ("IN_PLAY".equals(dadosExternos.getStatus()) &&
                        String.valueOf(Partida.StatusPartida.AGENDADA).equals(partida.getStatus())){
                    log.info("Partida ID {} mudou para EM_ANDAMENTO.", partida.getId());
                }

                if ("FINISHED".equals(dadosExternos.getStatus())) {
                    partida.finalizarPartida(dadosExternos.getPlacarCasa(), dadosExternos.getPlacarVisitante());
                    partidaPortOut.save(partida);
                    log.info("Partida ID {} finalizada com placar: {}x{}",
                            partida.getId(), dadosExternos.getPlacarCasa(), dadosExternos.getPlacarVisitante());
                }

            } catch (Exception e) {
                log.error("Falha ao sincronizar dados externos da partida local ID: {}. Erro: {}", partida.getId(), e.getMessage());
            }
        }
    }

}
