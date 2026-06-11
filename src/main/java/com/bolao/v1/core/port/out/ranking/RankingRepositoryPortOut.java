package com.bolao.v1.core.port.out.ranking;

import com.bolao.v1.core.domain.entity.ranking.Ranking;

import java.util.List;
import java.util.Optional;

public interface RankingRepositoryPortOut {

    Ranking save(Ranking ranking);

    Optional<Ranking> findByUserIdAndGrupoId(Long userId, Long grupoId);

    List<RankingComNome> findAllOrdenadoComNomeByGrupoId(Long grupoId);

    void deleteByGrupoIdAndUserIdNotIn(Long grupoId, List<Long> userIds);
}