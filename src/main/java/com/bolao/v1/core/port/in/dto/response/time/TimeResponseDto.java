package com.bolao.v1.core.port.in.dto.response.time;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeResponseDto {

    private Integer id;
    private String nome;
    private String logo;
}
