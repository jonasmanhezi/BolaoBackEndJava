package com.bolao.v1.core.port.out.palpite;

import com.bolao.v1.core.domain.entity.palpite.Palpite;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PalpiteRepositoryPortOut {


    Palpite save(Palpite palpite);

    Optional<Palpite> findById(Integer id);


    List<Palpite> findByPartidaId(Integer partidaId);

    List<Palpite> findByPartidaIdInAndUsuarioId(List<Integer> partidaIds, Integer usuarioId);

    Page<Palpite> findByUsuarioIdAndCampeonatoIdAndFaseIdPaged(
            Integer usuarioId,
            Integer campeonatoId,
            Integer faseId,
            int page,
            int size
    );

    List<UsuarioPontuacaoAggregate> sumPontuacaoPorUsuarioEmPartidasFinalizadas();

}
