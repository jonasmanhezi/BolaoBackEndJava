package com.bolao.v1.infrastructure.supabase.usuario;

import com.bolao.v1.infrastructure.model.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByEmail(String email);
    Optional<UsuarioEntity> findBySupabaseUserId(UUID supabaseUserId);
    boolean existsByEmail(String email);
    boolean existsBySupabaseUserId(UUID supabaseUserId);
}