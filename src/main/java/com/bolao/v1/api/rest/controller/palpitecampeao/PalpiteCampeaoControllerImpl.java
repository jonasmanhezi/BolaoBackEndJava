package com.bolao.v1.api.rest.controller.palpitecampeao;

import com.bolao.v1.core.domain.entity.palpitecampeao.PalpiteCampeao;
import com.bolao.v1.core.port.in.PalpiteCampeaoPortIn;
import com.bolao.v1.core.port.in.dto.request.palpitecampeao.PalpiteCampeaoCreateRequestDto;
import com.bolao.v1.core.port.in.dto.response.palpitecampeao.PalpiteCampeaoResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PalpiteCampeaoControllerImpl implements PalpiteCampeaoController {

    private final PalpiteCampeaoPortIn palpiteCampeaoService;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<PalpiteCampeaoResponseDto> registrar(Integer userId, PalpiteCampeaoCreateRequestDto request) {
        PalpiteCampeao palpite = palpiteCampeaoService.registrar(userId.longValue(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(palpite));
    }

    @Override
    public ResponseEntity<PalpiteCampeaoResponseDto> meuPalpite(Integer userId) {
        PalpiteCampeao palpite = palpiteCampeaoService.buscarMeuPalpite(userId.longValue());
        return ResponseEntity.ok(toDto(palpite));
    }

    private PalpiteCampeaoResponseDto toDto(PalpiteCampeao palpite) {
        return modelMapper.map(palpite, PalpiteCampeaoResponseDto.class);
    }
}
