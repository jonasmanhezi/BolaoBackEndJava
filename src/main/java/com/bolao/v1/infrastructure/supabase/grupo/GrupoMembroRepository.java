package com.bolao.v1.infrastructure.supabase.grupo;

import com.bolao.v1.infrastructure.model.GrupoEntity;
import com.bolao.v1.infrastructure.model.GrupoMembroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrupoMembroRepository extends JpaRepository<GrupoMembroEntity, Long> {

    boolean existsByGrupoIdAndUsuarioId(Long grupoId, Long usuarioId);

    Optional<GrupoMembroEntity> findByGrupoIdAndUsuarioId(Long grupoId, Long usuarioId);

    @Query("""
            SELECT g
            FROM GrupoEntity g
            INNER JOIN GrupoMembroEntity gm ON gm.grupoId = g.id
            WHERE gm.usuarioId = :usuarioId
            ORDER BY g.nome ASC
            """)
    List<GrupoEntity> findGruposByUsuarioId(@Param("usuarioId") Long usuarioId);
}