package com.bolao.v1.core.port.out.partidaExterna;

import com.bolao.v1.core.port.in.dto.response.partidaExterna.PartidaExternaDto;

public interface PartidaExternaPortOut {

    PartidaExternaDto buscarDadosPartidaExterna(Integer externalId);
}
