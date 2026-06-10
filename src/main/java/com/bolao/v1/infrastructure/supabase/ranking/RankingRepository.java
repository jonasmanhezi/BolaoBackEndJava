package com.bolao.v1.infrastructure.supabase.ranking;

import com.bolao.v1.infrastructure.model.RankingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<RankingEntity, Long> {

    Optional<RankingEntity> findByUserId(Long userId);

    List<RankingEntity> findAllByOrderByPontuacaoDescUserIdAsc();

    @Query("""
            SELECT r
            FROM RankingEntity r
            JOIN UsuarioEntity u ON u.id = r.userId
            ORDER BY r.pontuacao DESC, u.nome ASC
            """)
    List<RankingEntity> findAllOrdenadoPorPontuacao();

    @Query("""
            SELECT r.userId AS userId, r.pontuacao AS pontuacao, u.nome AS nome
            FROM RankingEntity r
            JOIN UsuarioEntity u ON u.id = r.userId
            ORDER BY r.pontuacao DESC, u.nome ASC
            """)
    List<RankingComNomeProjection> findAllOrdenadoComNome();
}