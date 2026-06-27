package com.bolao.v1.core.port.in;

import com.bolao.v1.core.domain.entity.palpitecampeao.PalpiteCampeao;
import com.bolao.v1.core.port.in.dto.request.palpitecampeao.PalpiteCampeaoCreateRequestDto;

public interface PalpiteCampeaoPortIn {

    PalpiteCampeao registrar(Long usuarioId, PalpiteCampeaoCreateRequestDto request);

    PalpiteCampeao buscarMeuPalpite(Long usuarioId);
}
