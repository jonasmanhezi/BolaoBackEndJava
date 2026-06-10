package com.bolao.v1.core.domain.entity.time;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Time {

    private Integer id;
    private String nome;
    private String logo;
}