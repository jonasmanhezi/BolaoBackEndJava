package com.bolao.v1.core.application.service.ranking;

import com.bolao.v1.core.domain.entity.ranking.Ranking;
import com.bolao.v1.core.port.in.RankingPortIn;
import com.bolao.v1.core.port.in.dto.response.ranking.RankingResponseDto;
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

    @Override
    @Transactional
    public void atualizarRanking() {
        log.info("Iniciando atualização do ranking a partir de palpites em partidas finalizadas");

        List<UsuarioPontuacaoAggregate> totais =
                palpiteRepositoryPortOut.sumPontuacaoPorUsuarioEmPartidasFinalizadas();

        List<Long> usuariosComPontuacao = new ArrayList<>();

        for (UsuarioPontuacaoAggregate aggregate : totais) {
            Long userId = aggregate.getUsuarioId();
            Integer pontuacao = aggregate.getPontuacaoTotal();
            usuariosComPontuacao.add(userId);

            Ranking ranking = rankingRepositoryPortOut.findByUserId(userId)
                    .map(existing -> {
                        existing.setPontuacao(pontuacao);
                        return existing;
                    })
                    .orElseGet(() -> Ranking.builder()
                            .userId(userId)
                            .pontuacao(pontuacao)
                            .createdAt(OffsetDateTime.now())
                            .build());

            rankingRepositoryPortOut.save(ranking);
        }

        rankingRepositoryPortOut.deleteByUserIdNotIn(usuariosComPontuacao);

        log.info("Ranking atualizado para {} usuários", usuariosComPontuacao.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RankingResponseDto> listarRanking() {
        List<RankingComNome> rankings = rankingRepositoryPortOut.findAllOrdenadoComNome();
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