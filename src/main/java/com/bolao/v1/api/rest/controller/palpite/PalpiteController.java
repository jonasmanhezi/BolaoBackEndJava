package com.bolao.v1.api.rest.controller.palpite;


import com.bolao.v1.core.port.in.dto.request.palpite.PalpiteCreateRequestDto;
import com.bolao.v1.core.port.in.dto.request.palpite.PalpiteUpdateRequestDto;
import com.bolao.v1.core.port.in.dto.response.palpite.PaginatedPalpiteResponseDto;
import com.bolao.v1.core.port.in.dto.response.palpite.PalpiteResponseDto;
import com.bolao.v1.security.AuthenticatedGrupoId;
import com.bolao.v1.security.AuthenticatedUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Palpites", description = "Requer JWT. Faça login em /auth/login, copie o token e clique em Authorize no Swagger.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/palpites")
public interface PalpiteController {

    @Operation(summary = "Registra um novo palpite para uma partida")
    @ApiResponse(responseCode = "201", description = "Palpite criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou jogo já iniciado")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    @PostMapping
    ResponseEntity<PalpiteResponseDto> create(
            @Parameter(hidden = true) @AuthenticatedUserId Integer userId,
            @Parameter(hidden = true) @AuthenticatedGrupoId Long grupoId,
            @RequestBody @Valid PalpiteCreateRequestDto request);

    @Operation(summary = "Atualiza o placar de um palpite existente")
    @ApiResponse(responseCode = "200", description = "Palpite atualizado com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado (O palpite não pertence ao usuário)")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    ResponseEntity<PalpiteResponseDto> atualizarPalpite(
            @PathVariable Integer id,
            @Parameter(hidden = true) @AuthenticatedUserId Integer userId,
            @Parameter(hidden = true) @AuthenticatedGrupoId Long grupoId,
            @RequestBody @Valid PalpiteUpdateRequestDto request);

    @Operation(summary = "Lista os palpites do usuário logado no grupo (campeonato/fase na URL são ignorados)")
    @ApiResponse(responseCode = "200", description = "Lista de palpites retornada com sucesso")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    @GetMapping("/campeonato/{campeonatoId}/fase/{faseId}")
    ResponseEntity<List<PalpiteResponseDto>> findByCampeonatoIdFaseId(
            @PathVariable Integer campeonatoId,
            @PathVariable Integer faseId,
            @Parameter(hidden = true) @AuthenticatedUserId Integer userId,
            @Parameter(hidden = true) @AuthenticatedGrupoId Long grupoId);

    @Operation(summary = "Lista palpites paginados do usuário no grupo (campeonato/fase na URL são ignorados)")
    @ApiResponse(responseCode = "200", description = "Página de palpites retornada com sucesso")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    @ApiResponse(responseCode = "403", description = "usuarioId da URL deve ser igual ao userId retornado no login")
    @GetMapping("/usuario/{usuarioId}/campeonato/{campeonatoId}/fase/{faseId}")
    ResponseEntity<PaginatedPalpiteResponseDto> findByUsuarioIdCampeonatoIdFaseIdPaged(
            @PathVariable Integer usuarioId,
            @PathVariable Integer campeonatoId,
            @PathVariable Integer faseId,
            @Parameter(hidden = true) @AuthenticatedUserId Integer userId,
            @Parameter(hidden = true) @AuthenticatedGrupoId Long grupoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size);

}