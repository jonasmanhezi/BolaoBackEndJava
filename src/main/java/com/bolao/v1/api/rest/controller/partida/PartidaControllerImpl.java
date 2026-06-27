package com.bolao.v1.api.rest.controller.partida;


import com.bolao.v1.api.rest.controller.partida.hateoas.HateoasResponseBuilder;
import com.bolao.v1.core.port.in.PartidaPortIn;
import com.bolao.v1.core.port.in.dto.request.partida.PartidaUpdateFinalizarDto;
import com.bolao.v1.core.port.in.dto.request.partida.PartidaUpdateRequestDto;
import com.bolao.v1.core.port.in.dto.response.partida.PartidaResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PartidaControllerImpl implements PartidaController {

    private final PartidaPortIn service;

    @Override
    public ResponseEntity<List<PartidaResponseDto>> getAll() {
        log.info("Listando todas as partidas");
        List<PartidaResponseDto> response = service.findAll();
        response.forEach(HateoasResponseBuilder::addLinks);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PartidaResponseDto> getById(Integer id) {
        log.info("Buscando partida por ID: {}", id);
        PartidaResponseDto response = service.findById(id);
        return ResponseEntity.ok(HateoasResponseBuilder.addLinks(response));
    }

    @Override
    public ResponseEntity<List<PartidaResponseDto>> getByFase(Integer faseId, Integer campeonatoId) {
        log.info("Filtrando partidas - Fase: {}, Campeonato: {}", faseId, campeonatoId);
        List<PartidaResponseDto> response = service.findByFaseId(faseId, campeonatoId);
        response.forEach(HateoasResponseBuilder::addLinks);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> iniciar(Integer id) {
        log.info("Solicitação para iniciar partida: {}", id);
        service.iniciarPartida(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> finalizar(Integer id, PartidaUpdateFinalizarDto request) {
        log.info("Solicitação para finalizar partida: {} com placar {}x{}",
                id, request.getGolsCasa(), request.getGolsVisitante());
        service.finalizarPartida(id, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> delete(Integer id) {
        log.info("Solicitação para deletar partida: {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}