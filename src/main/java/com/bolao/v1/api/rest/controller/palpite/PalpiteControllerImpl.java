package com.bolao.v1.api.rest.controller.palpite;

import com.bolao.v1.core.domain.entity.palpite.Palpite;
import com.bolao.v1.core.port.in.PalpitePortIn;
import com.bolao.v1.core.port.in.dto.request.palpite.PalpiteCreateRequestDto;
import com.bolao.v1.core.port.in.dto.request.palpite.PalpiteUpdateRequestDto;
import com.bolao.v1.core.port.in.dto.response.palpite.PaginatedPalpiteResponseDto;
import com.bolao.v1.core.port.in.dto.response.palpite.PalpiteResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.bolao.v1.security.AuthenticatedUserId;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PalpiteControllerImpl implements PalpiteController{

    private final PalpitePortIn palpiteService;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<PalpiteResponseDto> create(@AuthenticatedUserId Integer userId, PalpiteCreateRequestDto request) {
        Palpite palpiteSalvo = palpiteService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDto(palpiteSalvo));
    }

    @Override
    public ResponseEntity<PalpiteResponseDto> atualizarPalpite(Integer id, @AuthenticatedUserId Integer userId, PalpiteUpdateRequestDto request) {
        Palpite palpiteAtualizado = palpiteService.atualizarPalpite(id, userId, request);
        return ResponseEntity.ok(toResponseDto(palpiteAtualizado));
    }

    @Override
    public ResponseEntity<List<PalpiteResponseDto>> findByCampeonatoIdFaseId(Integer campeonatoId, Integer faseId, @AuthenticatedUserId Integer userId) {
        List<Palpite> palpites = palpiteService.findByCampeonatoIdFaseId(campeonatoId, faseId, userId);

        List<PalpiteResponseDto> responseList = palpites.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @Override
    public ResponseEntity<PaginatedPalpiteResponseDto> findByUsuarioIdCampeonatoIdFaseIdPaged(
            Integer usuarioId,
            Integer campeonatoId,
            Integer faseId,
            @AuthenticatedUserId Integer userId,
            int page,
            int size
    ) {
        PaginatedPalpiteResponseDto response = palpiteService.findByUsuarioIdCampeonatoIdFaseIdPaged(
                usuarioId, campeonatoId, faseId, userId, page, size
        );

        response.getContent().forEach(dto ->
                dto.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PalpiteController.class)
                                .atualizarPalpite(dto.getId(), dto.getUsuarioId(), null)
                ).withSelfRel())
        );

        return ResponseEntity.ok(response);
    }

    private PalpiteResponseDto toResponseDto(Palpite palpite) {
        PalpiteResponseDto dto = modelMapper.map(palpite, PalpiteResponseDto.class);

        dto.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PalpiteController.class)
                        .atualizarPalpite(palpite.getId(), palpite.getUsuarioId(), null)).withSelfRel());

        return dto;
    }
}

