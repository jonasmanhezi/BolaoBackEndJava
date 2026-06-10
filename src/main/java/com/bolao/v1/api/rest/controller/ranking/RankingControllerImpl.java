package com.bolao.v1.api.rest.controller.ranking;

import com.bolao.v1.core.port.in.RankingPortIn;
import com.bolao.v1.core.port.in.dto.response.ranking.RankingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RankingControllerImpl implements RankingController {

    private final RankingPortIn rankingService;

    @Override
    public ResponseEntity<List<RankingResponseDto>> listar() {
        return ResponseEntity.ok(rankingService.listarRanking());
    }

    @Override
    public ResponseEntity<Void> atualizar() {
        rankingService.atualizarRanking();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}