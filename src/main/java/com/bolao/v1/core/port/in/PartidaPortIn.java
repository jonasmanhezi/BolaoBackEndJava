package com.bolao.v1.core.port.in;

import com.bolao.v1.core.port.in.dto.request.partida.PartidaCreateRequestDto;
import com.bolao.v1.core.port.in.dto.request.partida.PartidaUpdateFinalizarDto;
import com.bolao.v1.core.port.in.dto.request.partida.PartidaUpdateRequestDto;
import com.bolao.v1.core.port.in.dto.response.partida.PartidaResponseDto;

import java.util.List;

public interface PartidaPortIn {


    void delete(Integer id);

    List<PartidaResponseDto> findByFaseId(Integer faseId, Integer campeonatoId);

    List<PartidaResponseDto> findAll();

    PartidaResponseDto findById(Integer id);

    void finalizarPartida(Integer id, PartidaUpdateFinalizarDto request);

    void iniciarPartida(Integer id);

    void sincronizarPartidasDoDia();

}
