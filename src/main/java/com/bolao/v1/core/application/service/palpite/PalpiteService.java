package com.bolao.v1.core.application.service.palpite;

import com.bolao.v1.core.domain.entity.palpite.Palpite;
import com.bolao.v1.core.domain.entity.partida.Partida;
import com.bolao.v1.core.port.in.PalpitePortIn;
import com.bolao.v1.core.port.in.dto.request.palpite.PalpiteCreateRequestDto;
import com.bolao.v1.core.port.in.dto.request.palpite.PalpiteUpdateRequestDto;
import com.bolao.v1.core.port.in.dto.response.palpite.PaginatedPalpiteResponseDto;
import com.bolao.v1.core.port.in.dto.response.palpite.PalpiteResponseDto;
import com.bolao.v1.core.port.out.grupo.GrupoRepositoryPortOut;
import com.bolao.v1.core.port.out.palpite.PalpiteRepositoryPortOut;
import com.bolao.v1.core.port.out.partida.PartidaRepositoryPortOut;
import com.bolao.v1.shared.palpite.PalpiteDeadlinePolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PalpiteService implements PalpitePortIn {

    private final PalpiteRepositoryPortOut palpiteRepositoryPortOut;
    private final PartidaRepositoryPortOut partidaRepositoryPortOut;
    private final GrupoRepositoryPortOut grupoRepositoryPortOut;
    private final ModelMapper modelMapper;

    @Override
    public Palpite create(Integer userId, Long grupoId, PalpiteCreateRequestDto request) {
        Partida partida = partidaRepositoryPortOut.findById(request.getPartidaId())
                .orElseThrow(() -> new RuntimeException("Partida não encontrada"));

        PalpiteDeadlinePolicy.validarEnvioPermitido(partida);

        if (palpiteRepositoryPortOut.existsByPartidaIdAndUsuarioIdAndGrupoId(
                request.getPartidaId(), userId, grupoId)) {
            throw new IllegalArgumentException("Você já enviou palpite para esta partida neste grupo.");
        }

        Palpite palpite = Palpite.builder()
                .usuarioId(userId)
                .grupoId(grupoId)
                .partidaId(request.getPartidaId())
                .golsCasa(request.getGolsCasa())
                .golsVisitante(request.getGolsVisitante())
                .palpiteWinnerId(request.getPalpiteWinnerId())
                .build();

        return palpiteRepositoryPortOut.save(palpite);
    }

    @Transactional
    @Override
    public Palpite atualizarPalpite(Integer palpiteId, Integer userId, Long grupoId, PalpiteUpdateRequestDto dto) {
        Palpite palpite = palpiteRepositoryPortOut.findById(palpiteId)
                .orElseThrow(() -> new RuntimeException("Palpite não encontrado"));

        if (!palpite.pertenceAoUsuario(userId)) {
            throw new IllegalArgumentException("Acesso negado: Este palpite não pertence a você.");
        }

        if (palpite.getGrupoId() == null || !palpite.getGrupoId().equals(grupoId)) {
            throw new IllegalArgumentException("Este palpite não pertence ao grupo informado.");
        }

        Partida partida = partidaRepositoryPortOut.findById(palpite.getPartidaId()).orElseThrow();

        PalpiteDeadlinePolicy.validarEnvioPermitido(partida);

        palpite.atualizarPalpite(
                dto.getGolsCasa(),
                dto.getGolsVisitante(),
                dto.getPalpiteWinnerId(),
                Partida.StatusPartida.valueOf(partida.getStatus())
        );

        return palpiteRepositoryPortOut.save(palpite);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Palpite> findByCampeonatoIdFaseId(
            Integer campeonatoId,
            Integer faseId,
            Integer userIdDoToken,
            Long grupoId
    ) {
        log.info(
                "Buscando palpites do usuário {} no grupo {} (campeonato {} e fase {} ignorados)",
                userIdDoToken, grupoId, campeonatoId, faseId
        );

        return palpiteRepositoryPortOut.findByUsuarioIdAndGrupoId(userIdDoToken, grupoId);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedPalpiteResponseDto findByUsuarioIdCampeonatoIdFaseIdPaged(
            Integer usuarioId,
            Integer campeonatoId,
            Integer faseId,
            Integer userIdAutenticado,
            Long grupoId,
            int page,
            int size
    ) {
        if (!usuarioId.equals(userIdAutenticado)
                && !grupoRepositoryPortOut.isMembro(grupoId, usuarioId.longValue())) {
            throw new IllegalArgumentException(
                    "Acesso negado: você só pode consultar palpites de membros do seu grupo."
            );
        }

        log.info(
                "Buscando palpites paginados do usuário {} no grupo {} (campeonato {} e fase {} ignorados, page={}, size={})",
                usuarioId, grupoId, campeonatoId, faseId, page, size
        );

        Page<Palpite> resultPage = palpiteRepositoryPortOut.findByUsuarioIdAndGrupoIdPaged(
                usuarioId, grupoId, page, size
        );

        List<PalpiteResponseDto> content = resultPage.getContent().stream()
                .map(palpite -> modelMapper.map(palpite, PalpiteResponseDto.class))
                .collect(Collectors.toList());

        return PaginatedPalpiteResponseDto.builder()
                .content(content)
                .page(resultPage.getNumber())
                .size(resultPage.getSize())
                .totalElements(resultPage.getTotalElements())
                .totalPages(resultPage.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public void pontuarPalpite(Integer partidaId, Integer golsCasaReal, Integer golsVisitanteReal, Integer faseId) {
        Partida partida = partidaRepositoryPortOut.findById(partidaId)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada: " + partidaId));

        log.info("Iniciando o motor de pontos para a partida ID: {}. Placar Oficial: {} x {}. Fase ID: {}. Pênalti: {}",
                partidaId, golsCasaReal, golsVisitanteReal, faseId, partida.isTemPenalti());

        List<Palpite> palpites = palpiteRepositoryPortOut.findByPartidaId(partidaId);

        if (palpites.isEmpty()) {
            log.info("Nenhum palpite registado para a partida ID: {}.", partidaId);
            return;
        }

        for (Palpite palpite : palpites) {
            palpite.pontuarPalpite(golsCasaReal, golsVisitanteReal, faseId,
                    partida.isTemPenalti(), partida.getWinnerId());
            palpiteRepositoryPortOut.save(palpite);
        }

        log.info("Pontuação calculada com sucesso para os {} palpites da partida {}.", palpites.size(), partidaId);
    }
}