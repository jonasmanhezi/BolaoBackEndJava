package com.bolao.v1.api.rest.controller.grupo;

import com.bolao.v1.core.port.in.dto.request.grupo.EntrarGrupoRequestDto;
import com.bolao.v1.core.port.in.dto.response.grupo.GrupoResponseDto;
import com.bolao.v1.security.AuthenticatedUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Grupos", description = "Entrada em bolões por código")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/grupos")
public interface GrupoController {

    @Operation(summary = "Entrar em um grupo usando o código")
    @PostMapping("/entrar")
    ResponseEntity<GrupoResponseDto> entrar(
            @Parameter(hidden = true) @AuthenticatedUserId Integer userId,
            @RequestBody @Valid EntrarGrupoRequestDto request
    );

    @Operation(summary = "Lista os grupos do usuário autenticado")
    @GetMapping("/meus")
    ResponseEntity<List<GrupoResponseDto>> listarMeus(
            @Parameter(hidden = true) @AuthenticatedUserId Integer userId
    );
}