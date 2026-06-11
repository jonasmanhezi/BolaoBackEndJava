package com.bolao.v1.infrastructure.supabase.grupo;

import com.bolao.v1.core.domain.entity.grupo.Grupo;
import com.bolao.v1.core.port.out.grupo.GrupoRepositoryPortOut;
import com.bolao.v1.infrastructure.model.GrupoEntity;
import com.bolao.v1.infrastructure.model.GrupoMembroEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GrupoPersistence implements GrupoRepositoryPortOut {

    private final GrupoRepository grupoRepository;
    private final GrupoMembroRepository grupoMembroRepository;

    @Override
    public Optional<Grupo> findByCodigo(String codigo) {
        return grupoRepository.findByCodigoIgnoreCase(codigo.trim()).map(this::toDomain);
    }

    @Override
    public Optional<Grupo> findById(Long id) {
        return grupoRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Grupo> findByUsuarioId(Long usuarioId) {
        return grupoMembroRepository.findGruposByUsuarioId(usuarioId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> findAllIds() {
        return grupoRepository.findAllIds();
    }

    @Override
    public void adicionarMembro(Long grupoId, Long usuarioId) {
        if (grupoMembroRepository.existsByGrupoIdAndUsuarioId(grupoId, usuarioId)) {
            return;
        }

        GrupoMembroEntity membro = GrupoMembroEntity.builder()
                .grupoId(grupoId)
                .usuarioId(usuarioId)
                .createdAt(OffsetDateTime.now())
                .build();

        grupoMembroRepository.save(membro);
    }

    @Override
    public boolean isMembro(Long grupoId, Long usuarioId) {
        return grupoMembroRepository.existsByGrupoIdAndUsuarioId(grupoId, usuarioId);
    }

    private Grupo toDomain(GrupoEntity entity) {
        return Grupo.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nome(entity.getNome())
                .campeonatoId(entity.getCampeonatoId())
                .build();
    }
}