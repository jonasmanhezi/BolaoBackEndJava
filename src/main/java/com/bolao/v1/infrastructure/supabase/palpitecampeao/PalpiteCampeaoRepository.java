package com.bolao.v1.infrastructure.supabase.palpitecampeao;

import com.bolao.v1.infrastructure.model.PalpiteCampeaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PalpiteCampeaoRepository extends JpaRepository<PalpiteCampeaoEntity, Long> {

    boolean existsByUsuarioId(Long usuarioId);

    Optional<PalpiteCampeaoEntity> findByUsuarioId(Long usuarioId);
}
