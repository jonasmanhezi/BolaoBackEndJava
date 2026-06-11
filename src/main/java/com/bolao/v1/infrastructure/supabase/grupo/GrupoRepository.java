package com.bolao.v1.infrastructure.supabase.grupo;

import com.bolao.v1.infrastructure.model.GrupoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrupoRepository extends JpaRepository<GrupoEntity, Long> {

    Optional<GrupoEntity> findByCodigoIgnoreCase(String codigo);

    @Query("SELECT g.id FROM GrupoEntity g")
    List<Long> findAllIds();
}