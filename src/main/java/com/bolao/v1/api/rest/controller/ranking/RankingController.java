package com.bolao.v1.api.rest.controller.ranking;

import com.bolao.v1.core.port.in.dto.response.ranking.RankingResponseDto;
import com.bolao.v1.security.AuthenticatedGrupoId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Ranking", description = "Classificação por grupo")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/ranking")
public interface RankingController {

    @Operation(summary = "Lista o ranking do grupo ativo")
    @ApiResponse(responseCode = "200", description = "Ranking retornado com sucesso")
    @GetMapping
    ResponseEntity<List<RankingResponseDto>> listar(
            @Parameter(hidden = true) @AuthenticatedGrupoId Long grupoId
    );

    @Operation(summary = "Recalcula o ranking a partir dos palpites em partidas finalizadas")
    @ApiResponse(responseCode = "204", description = "Ranking atualizado com sucesso")
    @PostMapping("/atualizar")
    ResponseEntity<Void> atualizar();
}