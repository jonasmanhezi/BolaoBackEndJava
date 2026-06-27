package com.bolao.v1.core.application.service.campeonato;

import com.bolao.v1.core.domain.entity.campeonato.Campeonato;
import com.bolao.v1.core.port.in.CampeonatoPortIn;
import com.bolao.v1.core.port.in.dto.request.campeonato.CampeonatoCreateRequestDto;
import com.bolao.v1.core.port.in.dto.request.campeonato.CampeonatoUpdateRequestDto;
import com.bolao.v1.core.port.in.dto.response.campeonato.CampeonatoResponseDto;
import com.bolao.v1.core.port.out.campeonato.CampeonatoRepositoryPortOut;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CampeonatoService implements CampeonatoPortIn {


    private final CampeonatoRepositoryPortOut repositoryPortOut;
    private final ModelMapper mapper;

    @Override
    public CampeonatoResponseDto create(CampeonatoCreateRequestDto request) {

        Campeonato campeonato = mapper.map(request, Campeonato.class);
        Campeonato saved = repositoryPortOut.save(campeonato);

        return mapper.map(saved, CampeonatoResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public CampeonatoResponseDto findById(Integer id) {
        Campeonato campeonato = repositoryPortOut.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Campeonato não encontrado: " + id));
        return mapper.map(campeonato, CampeonatoResponseDto.class);
    }


    @Override
    @Transactional(readOnly = true)
    public List<CampeonatoResponseDto> findAll() {
        return repositoryPortOut.findAll().stream()
        .map(campeonato -> mapper.map(campeonato, CampeonatoResponseDto.class))
                .collect(Collectors.toList());


    }

    @Override
    public CampeonatoResponseDto update(Integer id, CampeonatoUpdateRequestDto request) {
        Campeonato existing = repositoryPortOut.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Campeonato não encontrado: " + id));

        mapper.map(request, existing);

        Campeonato updated = repositoryPortOut.save(existing);
        return mapper.map(updated, CampeonatoResponseDto.class);
    }

    @Override
    public void delete(Integer id) {
        if (!repositoryPortOut.existsById(id)) {
            throw new EntityNotFoundException("Campeonato não encontrado: " + id);
        }
        repositoryPortOut.deleteById(id);
    }
}
