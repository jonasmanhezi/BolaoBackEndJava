package com.bolao.v1.api.rest.controller.grupo;

import com.bolao.v1.core.domain.entity.grupo.Grupo;
import com.bolao.v1.core.port.in.GrupoPortIn;
import com.bolao.v1.core.port.in.dto.request.grupo.EntrarGrupoRequestDto;
import com.bolao.v1.core.port.in.dto.response.grupo.GrupoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GrupoControllerImpl implements GrupoController {

    private final GrupoPortIn grupoService;

    @Override
    public ResponseEntity<GrupoResponseDto> entrar(Integer userId, EntrarGrupoRequestDto request) {
        Grupo grupo = grupoService.entrarComCodigo(userId, request);
        return ResponseEntity.ok(toDto(grupo));
    }

    @Override
    public ResponseEntity<List<GrupoResponseDto>> listarMeus(Integer userId) {
        List<GrupoResponseDto> grupos = grupoService.listarMeusGrupos(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(grupos);
    }

    private GrupoResponseDto toDto(Grupo grupo) {
        return GrupoResponseDto.builder()
                .id(grupo.getId())
                .codigo(grupo.getCodigo())
                .nome(grupo.getNome())
                .campeonatoId(grupo.getCampeonatoId())
                .build();
    }
}