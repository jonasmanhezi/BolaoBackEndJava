package com.bolao.v1.core.port.in.dto.request.palpite;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PalpiteUpdateRequestDto {

    private Integer id;
    private Integer golsCasa;
    private Integer golsVisitante;
}
