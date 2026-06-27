package com.bolao.v1.infrastructure.supabase.time;

import com.bolao.v1.core.domain.entity.time.Time;
import com.bolao.v1.core.port.out.time.TimeRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TimePersistence implements TimeRepositoryPortOut {

    private final TimeRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public List<Time> findAll() {
        return repository.findAll().stream()
                .map(entity -> modelMapper.map(entity, Time.class))
                .collect(Collectors.toList());
    }
}
