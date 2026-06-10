package com.bolao.v1.core.port.out.usuario;

import com.bolao.v1.core.domain.entity.usuario.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepositoryPortOut {

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findBySupabaseUserId(UUID supabaseUserId);

    Usuario save(Usuario usuario);

    boolean existsByEmail(String email);

    boolean existsBySupabaseUserId(UUID supabaseUserId);
}