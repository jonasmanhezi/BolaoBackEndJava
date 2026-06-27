package com.bolao.v1.core.application.service.time;

import com.bolao.v1.core.domain.entity.time.Time;
import com.bolao.v1.core.port.in.TimePortIn;
import com.bolao.v1.core.port.out.time.TimeRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeService implements TimePortIn {

    private final TimeRepositoryPortOut repositoryPortOut;

    @Override
    @Transactional(readOnly = true)
    public List<Time> listarTodos() {
        return repositoryPortOut.findAll();
    }
}
