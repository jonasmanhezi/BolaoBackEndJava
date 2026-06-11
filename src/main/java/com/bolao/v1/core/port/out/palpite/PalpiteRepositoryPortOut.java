package com.bolao.v1.core.port.out.palpite;

import com.bolao.v1.core.domain.entity.palpite.Palpite;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PalpiteRepositoryPortOut {

    Palpite save(Palpite palpite);

    Optional<Palpite> findById(Integer id);

    List<Palpite> findByPartidaId(Integer partidaId);

    List<Palpite> findByPartidaIdInAndUsuarioIdAndGrupoId(
            List<Integer> partidaIds,
            Integer usuarioId,
            Long grupoId
    );

    boolean existsByPartidaIdAndUsuarioIdAndGrupoId(Integer partidaId, Integer usuarioId, Long grupoId);

    List<Palpite> findByUsuarioIdAndGrupoId(Integer usuarioId, Long grupoId);

    Page<Palpite> findByUsuarioIdAndGrupoIdPaged(
            Integer usuarioId,
            Long grupoId,
            int page,
            int size
    );

    List<UsuarioPontuacaoAggregate> sumPontuacaoPorUsuarioEmPartidasFinalizadasPorGrupo(Long grupoId);
}