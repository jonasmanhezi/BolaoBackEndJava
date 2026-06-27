package com.bolao.v1.core.port.out.palpitecampeao;

import com.bolao.v1.core.domain.entity.palpitecampeao.PalpiteCampeao;

import java.util.Optional;

public interface PalpiteCampeaoRepositoryPortOut {

    PalpiteCampeao save(PalpiteCampeao palpiteCampeao);

    boolean existsByUsuarioId(Long usuarioId);

    Optional<PalpiteCampeao> findByUsuarioId(Long usuarioId);
}
