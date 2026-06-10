package com.bolao.v1.core.port.in.dto.response.palpite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedPalpiteResponseDto {

    private List<PalpiteResponseDto> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}