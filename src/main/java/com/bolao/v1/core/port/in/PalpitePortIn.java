package com.bolao.v1.core.port.in;

import com.bolao.v1.core.domain.entity.palpite.Palpite;
import com.bolao.v1.core.port.in.dto.request.palpite.PalpiteCreateRequestDto;
import com.bolao.v1.core.port.in.dto.request.palpite.PalpiteUpdateRequestDto;
import com.bolao.v1.core.port.in.dto.response.palpite.PaginatedPalpiteResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PalpitePortIn {

    void pontuarPalpite(Integer partidaId, Integer golsCasaReal, Integer golsVisitanteReal);

    Palpite create(Integer userId, Long grupoId, PalpiteCreateRequestDto request);

    @Transactional
    Palpite atualizarPalpite(Integer palpiteId, Integer userId, Long grupoId, PalpiteUpdateRequestDto dto);

    @Transactional(readOnly = true)
    List<Palpite> findByCampeonatoIdFaseId(
            Integer campeonatoId,
            Integer faseId,
            Integer userIdDoToken,
            Long grupoId
    );

    @Transactional(readOnly = true)
    PaginatedPalpiteResponseDto findByUsuarioIdCampeonatoIdFaseIdPaged(
            Integer usuarioId,
            Integer campeonatoId,
            Integer faseId,
            Integer userIdAutenticado,
            Long grupoId,
            int page,
            int size
    );
}