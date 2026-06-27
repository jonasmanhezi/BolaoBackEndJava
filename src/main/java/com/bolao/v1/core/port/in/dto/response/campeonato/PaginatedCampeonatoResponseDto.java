package com.bolao.v1.core.port.in.dto.response.campeonato;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PaginatedCampeonatoResponseDto {

    private List<CampeonatoResponseDto> content;

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;

}
