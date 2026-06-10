package com.bolao.v1.api.rest.controller.partida;


import com.bolao.v1.core.port.in.dto.request.partida.PartidaUpdateFinalizarDto;
import com.bolao.v1.core.port.in.dto.request.partida.PartidaUpdateRequestDto;
import com.bolao.v1.core.port.in.dto.response.partida.PartidaResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Partidas", description = "Operações para gerenciamento do ciclo de vida das partidas")
@RequestMapping("/v1/partidas")
public interface PartidaController {

    @Operation(summary = "Lista todas as partidas cadastradas")
    @GetMapping
    ResponseEntity<List<PartidaResponseDto>> getAll();

    @Operation(summary = "Busca uma partida pelo ID")
    @ApiResponse(responseCode = "200", description = "Partida encontrada")
    @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    @GetMapping("/{id}")
    ResponseEntity<PartidaResponseDto> getById(@PathVariable Integer id);

    @Operation(summary = "Filtra partidas por Fase e Campeonato")
    @GetMapping("/fase/{faseId}/campeonato/{campeonatoId}")
    ResponseEntity<List<PartidaResponseDto>> getByFase(@PathVariable Integer faseId, @PathVariable Integer campeonatoId);

    @Operation(summary = "Inicia a partida (Status: EM_ANDAMENTO)")
    @ApiResponse(responseCode = "204", description = "Partida iniciada com sucesso")
    @PatchMapping("/{id}/iniciar")
    ResponseEntity<Void> iniciar(@PathVariable Integer id);

    @Operation(summary = "Finaliza a partida com placar oficial (Status: FINALIZADA)")
    @ApiResponse(responseCode = "204", description = "Partida finalizada e placar registrado")
    @PutMapping("/{id}/finalizar")
    ResponseEntity<Void> finalizar(@PathVariable Integer id, @RequestBody @Valid PartidaUpdateFinalizarDto request);

    @Operation(summary = "Deleta uma partida")
    @ApiResponse(responseCode = "204", description = "Partida removida")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Integer id);
}
