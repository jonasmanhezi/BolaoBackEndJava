package com.bolao.v1.infrastructure.supabase.ranking;

import com.bolao.v1.core.domain.entity.ranking.Ranking;
import com.bolao.v1.core.port.out.ranking.RankingComNome;
import com.bolao.v1.core.port.out.ranking.RankingRepositoryPortOut;
import com.bolao.v1.infrastructure.model.RankingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RankingPersistence implements RankingRepositoryPortOut {

    private final RankingRepository repository;

    @Override
    public Ranking save(Ranking ranking) {
        RankingEntity entity = toEntity(ranking);
        RankingEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Ranking> findByUserIdAndGrupoId(Long userId, Long grupoId) {
        return repository.findByUserIdAndGrupoId(userId, grupoId).map(this::toDomain);
    }

    @Override
    public List<RankingComNome> findAllOrdenadoComNomeByGrupoId(Long grupoId) {
        return repository.findAllOrdenadoComNomeByGrupoId(grupoId).stream()
                .map(row -> RankingComNome.builder()
                        .userId(row.getUserId())
                        .pontuacao(row.getPontuacao() == null ? null : row.getPontuacao().intValue())
                        .nome(row.getNome())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByGrupoIdAndUserIdNotIn(Long grupoId, List<Long> userIds) {
        List<RankingEntity> rankings = repository.findByGrupoId(grupoId);

        if (userIds == null || userIds.isEmpty()) {
            rankings.forEach(entity -> repository.deleteById(entity.getId()));
            return;
        }

        rankings.stream()
                .filter(entity -> entity.getUserId() != null && !userIds.contains(entity.getUserId()))
                .forEach(entity -> repository.deleteById(entity.getId()));
    }

    private RankingEntity toEntity(Ranking ranking) {
        return RankingEntity.builder()
                .id(ranking.getId())
                .userId(ranking.getUserId())
                .grupoId(ranking.getGrupoId())
                .createdAt(ranking.getCreatedAt())
                .pontuacao(ranking.getPontuacao() == null ? null : ranking.getPontuacao().shortValue())
                .build();
    }

    private Ranking toDomain(RankingEntity entity) {
        return Ranking.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .grupoId(entity.getGrupoId())
                .createdAt(entity.getCreatedAt())
                .pontuacao(entity.getPontuacao() == null ? null : entity.getPontuacao().intValue())
                .build();
    }
}