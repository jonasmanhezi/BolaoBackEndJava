package com.bolao.v1.api.rest.controller.campeonato;


import com.bolao.v1.core.port.in.CampeonatoPortIn;
import com.bolao.v1.core.port.in.dto.request.campeonato.CampeonatoCreateRequestDto;
import com.bolao.v1.core.port.in.dto.response.campeonato.CampeonatoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/campeonatos")
@RequiredArgsConstructor
@Slf4j
public class CampeonatoControllerImpl implements CampeonatoController {

    private final CampeonatoPortIn campeonatoPortIn;
    @Override
    public ResponseEntity<CampeonatoResponseDto> getById(Integer id) {
        log.info("Recebida requisição para buscar campeonato com ID: {}", id);
        CampeonatoResponseDto response = campeonatoPortIn.findById(id);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<CampeonatoResponseDto>> getAll() {
        log.info("Recebida requisição para listar campeonatos");
        List<CampeonatoResponseDto> response = campeonatoPortIn.findAll();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CampeonatoResponseDto> create(CampeonatoCreateRequestDto request) {
        log.info("Recebida requisição para criar campeonato: {}", request.getNome());
        CampeonatoResponseDto response = campeonatoPortIn.create(request);

        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @Override
    public ResponseEntity<Void> delete(Integer id) {
        log.info("Recebida requisição para deletar campeonato com ID: {}", id);
        campeonatoPortIn.delete(id);

        return ResponseEntity.noContent().build();
    }
}

