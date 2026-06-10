package com.bolao.v1.infrastructure.supabase.usuario;

import com.bolao.v1.core.domain.entity.usuario.Usuario;
import com.bolao.v1.core.port.out.usuario.UsuarioRepositoryPortOut;
import com.bolao.v1.infrastructure.model.UsuarioEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UsuarioPersistence implements UsuarioRepositoryPortOut {

    private final UsuarioRepository repository;
    private final ModelMapper mapper;

    @Override
    public Optional<Usuario> findById(Long id) {
        return repository.findById(id)
                .map(entity -> mapper.map(entity, Usuario.class));
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(entity -> mapper.map(entity, Usuario.class));
    }

    @Override
    public Optional<Usuario> findBySupabaseUserId(UUID supabaseUserId) {
        return repository.findBySupabaseUserId(supabaseUserId)
                .map(entity -> mapper.map(entity, Usuario.class));
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = mapper.map(usuario, UsuarioEntity.class);
        UsuarioEntity saved = repository.save(entity);
        return mapper.map(saved, Usuario.class);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsBySupabaseUserId(UUID supabaseUserId) {
        return repository.existsBySupabaseUserId(supabaseUserId);
    }
}