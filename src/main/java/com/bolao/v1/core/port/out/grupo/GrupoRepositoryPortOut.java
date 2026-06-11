package com.bolao.v1.core.port.out.grupo;

import com.bolao.v1.core.domain.entity.grupo.Grupo;

import java.util.List;
import java.util.Optional;

public interface GrupoRepositoryPortOut {

    Optional<Grupo> findByCodigo(String codigo);

    Optional<Grupo> findById(Long id);

    List<Grupo> findByUsuarioId(Long usuarioId);

    List<Long> findAllIds();

    void adicionarMembro(Long grupoId, Long usuarioId);

    boolean isMembro(Long grupoId, Long usuarioId);
}