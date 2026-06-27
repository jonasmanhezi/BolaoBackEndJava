package com.bolao.v1.api.rest.controller.palpitecampeao;

import com.bolao.v1.core.port.in.dto.request.palpitecampeao.PalpiteCampeaoCreateRequestDto;
import com.bolao.v1.core.port.in.dto.response.palpitecampeao.PalpiteCampeaoResponseDto;
import com.bolao.v1.security.AuthenticatedUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Palpite Campeão", description = "Requer JWT. Cada usuário pode palpitar o campeão uma única vez.")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/palpites-campeao")
public interface PalpiteCampeaoController {

    @Operation(summary = "Registra o palpite de campeão do torneio (apenas uma vez por usuário)")
    @ApiResponse(responseCode = "201", description = "Palpite registrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Usuário já registrou um palpite de campeão")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    @PostMapping
    ResponseEntity<PalpiteCampeaoResponseDto> registrar(
            @Parameter(hidden = true) @AuthenticatedUserId Integer userId,
            @RequestBody @Valid PalpiteCampeaoCreateRequestDto request);

    @Operation(summary = "Retorna o palpite de campeão do usuário logado")
    @ApiResponse(responseCode = "200", description = "Palpite encontrado")
    @ApiResponse(responseCode = "404", description = "Nenhum palpite registrado")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    @GetMapping("/meu-palpite")
    ResponseEntity<PalpiteCampeaoResponseDto> meuPalpite(
            @Parameter(hidden = true) @AuthenticatedUserId Integer userId);
}
