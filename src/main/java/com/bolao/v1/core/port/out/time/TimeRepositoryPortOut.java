package com.bolao.v1.core.port.out.time;

import com.bolao.v1.core.domain.entity.time.Time;

import java.util.List;

public interface TimeRepositoryPortOut {

    List<Time> findAll();
}
