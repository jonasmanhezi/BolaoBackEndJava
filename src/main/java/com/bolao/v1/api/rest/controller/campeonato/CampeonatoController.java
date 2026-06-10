package com.bolao.v1.api.rest.controller.campeonato;

import com.bolao.v1.core.port.in.dto.request.campeonato.CampeonatoCreateRequestDto;
import com.bolao.v1.core.port.in.dto.response.campeonato.CampeonatoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Campeonato API", description = "REST API para gerenciar campeonatos")
public interface CampeonatoController {

    @Operation(summary = "Busca um campeonato pelo ID")
    @ApiResponse(responseCode = "200", description = "Campeonato encontrado")
    @ApiResponse(responseCode = "404", description = "Campeonato não encontrado")
    @GetMapping("/{id}")
    ResponseEntity<CampeonatoResponseDto> getById(@PathVariable Integer id);

    @Operation(summary = "Lista todos os campeonatos")
    @GetMapping
    ResponseEntity<List<CampeonatoResponseDto>> getAll();

    @Operation(summary = "Cria um novo campeonato")
    @ApiResponse(responseCode = "201", description = "Campeonato criado com sucesso")
    @PostMapping
    ResponseEntity<CampeonatoResponseDto> create(@RequestBody @Valid CampeonatoCreateRequestDto request);

    @Operation(summary = "Deleta um campeonato")
    @ApiResponse(responseCode = "204", description = "Campeonato deletado")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Integer id);
}
