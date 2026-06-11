package com.bolao.v1.core.application.service.ranking;

import com.bolao.v1.core.domain.entity.ranking.Ranking;
import com.bolao.v1.core.port.in.RankingPortIn;
import com.bolao.v1.core.port.in.dto.response.ranking.RankingResponseDto;
import com.bolao.v1.core.port.out.grupo.GrupoRepositoryPortOut;
import com.bolao.v1.core.port.out.palpite.PalpiteRepositoryPortOut;
import com.bolao.v1.core.port.out.palpite.UsuarioPontuacaoAggregate;
import com.bolao.v1.core.port.out.ranking.RankingComNome;
import com.bolao.v1.core.port.out.ranking.RankingRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankingService implements RankingPortIn {

    private final PalpiteRepositoryPortOut palpiteRepositoryPortOut;
    private final RankingRepositoryPortOut rankingRepositoryPortOut;
    private final GrupoRepositoryPortOut grupoRepositoryPortOut;

    @Override
    @Transactional
    public void atualizarRanking() {
        List<Long> grupoIds = grupoRepositoryPortOut.findAllIds();

        if (grupoIds.isEmpty()) {
            log.info("Nenhum grupo cadastrado para atualizar ranking.");
            return;
        }

        for (Long grupoId : grupoIds) {
            atualizarRankingDoGrupo(grupoId);
        }
    }

    @Override
    @Transactional
    public void atualizarRankingDoGrupo(Long grupoId) {
        log.info("Atualizando ranking do grupo {}", grupoId);

        List<UsuarioPontuacaoAggregate> totais =
                palpiteRepositoryPortOut.sumPontuacaoPorUsuarioEmPartidasFinalizadasPorGrupo(grupoId);

        List<Long> usuariosComPontuacao = new ArrayList<>();

        for (UsuarioPontuacaoAggregate aggregate : totais) {
            Long userId = aggregate.getUsuarioId();
            Integer pontuacao = aggregate.getPontuacaoTotal();
            usuariosComPontuacao.add(userId);

            Ranking ranking = rankingRepositoryPortOut.findByUserIdAndGrupoId(userId, grupoId)
                    .map(existing -> {
                        existing.setPontuacao(pontuacao);
                        return existing;
                    })
                    .orElseGet(() -> Ranking.builder()
                            .userId(userId)
                            .grupoId(grupoId)
                            .pontuacao(pontuacao)
                            .createdAt(OffsetDateTime.now())
                            .build());

            rankingRepositoryPortOut.save(ranking);
        }

        rankingRepositoryPortOut.deleteByGrupoIdAndUserIdNotIn(grupoId, usuariosComPontuacao);
        log.info("Ranking do grupo {} atualizado para {} usuários", grupoId, usuariosComPontuacao.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RankingResponseDto> listarRanking(Long grupoId) {
        List<RankingComNome> rankings = rankingRepositoryPortOut.findAllOrdenadoComNomeByGrupoId(grupoId);
        List<RankingResponseDto> response = new ArrayList<>();

        int posicao = 1;
        for (RankingComNome ranking : rankings) {
            response.add(RankingResponseDto.builder()
                    .posicao(posicao++)
                    .userId(ranking.getUserId())
                    .nome(ranking.getNome())
                    .pontuacao(ranking.getPontuacao())
                    .build());
        }

        return response;
    }
}