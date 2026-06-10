package com.bolao.v1.core.port.out.ranking;

import com.bolao.v1.core.domain.entity.ranking.Ranking;

import java.util.List;
import java.util.Optional;

public interface RankingRepositoryPortOut {

    Ranking save(Ranking ranking);

    Optional<Ranking> findByUserId(Long userId);

    List<Ranking> findAllOrdenadoPorPontuacao();

    List<RankingComNome> findAllOrdenadoComNome();

    void deleteByUserIdNotIn(List<Long> userIds);
}