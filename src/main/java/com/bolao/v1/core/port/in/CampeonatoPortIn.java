package com.bolao.v1.core.port.in;

import com.bolao.v1.core.domain.entity.campeonato.Campeonato;
import com.bolao.v1.core.port.in.dto.request.campeonato.CampeonatoCreateRequestDto;
import com.bolao.v1.core.port.in.dto.request.campeonato.CampeonatoUpdateRequestDto;
import com.bolao.v1.core.port.in.dto.response.campeonato.CampeonatoFilterDto;
import com.bolao.v1.core.port.in.dto.response.campeonato.CampeonatoResponseDto;
import com.bolao.v1.core.port.in.dto.response.campeonato.PaginatedCampeonatoResponseDto;

import java.awt.print.Pageable;
import java.util.List;

public interface CampeonatoPortIn {

    CampeonatoResponseDto create(CampeonatoCreateRequestDto request);

    CampeonatoResponseDto update(Integer id, CampeonatoUpdateRequestDto request);

    void delete(Integer id);

    List<CampeonatoResponseDto> findAll();

    CampeonatoResponseDto findById(Integer id);

}
