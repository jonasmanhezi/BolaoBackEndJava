package com.bolao.v1.infrastructure.supabase.palpite;

import com.bolao.v1.infrastructure.model.PalpiteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PalpiteRepository extends JpaRepository<PalpiteEntity, Integer> {

    List<PalpiteEntity> findByPartidaId(Integer partidaId);

    List<PalpiteEntity> findByPartidaIdInAndUsuarioIdAndGrupoId(
            List<Integer> partidaIds,
            Integer usuarioId,
            Long grupoId
    );

    boolean existsByPartidaIdAndUsuarioIdAndGrupoId(Integer partidaId, Integer usuarioId, Long grupoId);

    List<PalpiteEntity> findByUsuarioIdAndGrupoIdOrderByIdAsc(Integer usuarioId, Long grupoId);

    Page<PalpiteEntity> findByUsuarioIdAndGrupoIdOrderByIdAsc(
            Integer usuarioId,
            Long grupoId,
            Pageable pageable
    );

    @Query("""
            SELECT p FROM PalpiteEntity p, PartidaEntity pt
            WHERE p.partidaId = pt.id
              AND p.usuarioId = :usuarioId
              AND p.grupoId = :grupoId
              AND pt.campeonatoId = :campeonatoId
              AND pt.faseId = :faseId
            ORDER BY p.id ASC
            """)
    Page<PalpiteEntity> findByUsuarioIdAndGrupoIdAndCampeonatoIdAndFaseId(
            @Param("usuarioId") Integer usuarioId,
            @Param("grupoId") Long grupoId,
            @Param("campeonatoId") Integer campeonatoId,
            @Param("faseId") Integer faseId,
            Pageable pageable
    );

    @Query("""
            SELECT p.usuarioId AS usuarioId, COALESCE(SUM(p.pontuacaoObtida), 0) AS pontuacaoTotal
            FROM PalpiteEntity p
            INNER JOIN PartidaEntity pt ON p.partidaId = pt.id
            WHERE pt.status = 'FINALIZADA'
              AND p.grupoId = :grupoId
            GROUP BY p.usuarioId
            """)
    List<UsuarioPontuacaoSum> sumPontuacaoPorUsuarioEmPartidasFinalizadasPorGrupo(@Param("grupoId") Long grupoId);
}