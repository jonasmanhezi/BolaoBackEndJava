package com.bolao.v1.api.rest.controller.time;

import com.bolao.v1.core.port.in.TimePortIn;
import com.bolao.v1.core.port.in.dto.response.time.TimeResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TimeControllerImpl implements TimeController {

    private final TimePortIn timeService;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<List<TimeResponseDto>> listarTodos() {
        List<TimeResponseDto> times = timeService.listarTodos().stream()
                .map(time -> modelMapper.map(time, TimeResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(times);
    }
}
