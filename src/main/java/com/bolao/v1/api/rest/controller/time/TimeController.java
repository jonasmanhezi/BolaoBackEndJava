package com.bolao.v1.api.rest.controller.time;

import com.bolao.v1.core.port.in.dto.response.time.TimeResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Times", description = "Lista os times disponíveis no torneio")
@RequestMapping("/v1/times")
public interface TimeController {

    @Operation(summary = "Lista todos os times com id, nome e logo")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    ResponseEntity<List<TimeResponseDto>> listarTodos();
}
