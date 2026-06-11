package com.bolao.v1.core.port.in;

import com.bolao.v1.core.port.in.dto.response.ranking.RankingResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RankingPortIn {

    @Transactional
    void atualizarRanking();

    @Transactional
    void atualizarRankingDoGrupo(Long grupoId);

    @Transactional(readOnly = true)
    List<RankingResponseDto> listarRanking(Long grupoId);
}