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
    public Optional<Ranking> findByUserId(Long userId) {
        return repository.findByUserId(userId).map(this::toDomain);
    }

    @Override
    public List<Ranking> findAllOrdenadoPorPontuacao() {
        return repository.findAllOrdenadoPorPontuacao().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RankingComNome> findAllOrdenadoComNome() {
        return repository.findAllOrdenadoComNome().stream()
                .map(row -> RankingComNome.builder()
                        .userId(row.getUserId())
                        .pontuacao(row.getPontuacao() == null ? null : row.getPontuacao().intValue())
                        .nome(row.getNome())
                        .build())
                .collect(Collectors.toList());
    }

    private RankingEntity toEntity(Ranking ranking) {
        return RankingEntity.builder()
                .id(ranking.getId())
                .userId(ranking.getUserId())
                .createdAt(ranking.getCreatedAt())
                .pontuacao(ranking.getPontuacao() == null ? null : ranking.getPontuacao().shortValue())
                .build();
    }

    private Ranking toDomain(RankingEntity entity) {
        return Ranking.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .createdAt(entity.getCreatedAt())
                .pontuacao(entity.getPontuacao() == null ? null : entity.getPontuacao().intValue())
                .build();
    }

    @Override
    public void deleteByUserIdNotIn(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            repository.deleteAll();
            return;
        }
        repository.findAll().stream()
                .filter(entity -> entity.getUserId() != null && !userIds.contains(entity.getUserId()))
                .forEach(entity -> repository.deleteById(entity.getId()));
    }
}