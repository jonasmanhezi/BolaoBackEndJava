package com.bolao.v1.infrastructure.supabase.palpite;

import com.bolao.v1.core.domain.entity.palpite.Palpite;
import com.bolao.v1.core.port.out.palpite.PalpiteRepositoryPortOut;
import com.bolao.v1.core.port.out.palpite.UsuarioPontuacaoAggregate;
import com.bolao.v1.infrastructure.model.PalpiteEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PalpitePersistence implements PalpiteRepositoryPortOut {

    private final PalpiteRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public Palpite save(Palpite palpite) {
        PalpiteEntity entity = modelMapper.map(palpite, PalpiteEntity.class);
        PalpiteEntity entitySalva = repository.save(entity);
        return modelMapper.map(entitySalva, Palpite.class);
    }

    @Override
    public Optional<Palpite> findById(Integer id) {
        return repository.findById(id)
                .map(entity -> modelMapper.map(entity, Palpite.class));
    }

    @Override
    public List<Palpite> findByPartidaId(Integer partidaId) {
        return repository.findByPartidaId(partidaId).stream()
                .map(entity -> modelMapper.map(entity, Palpite.class))
                .collect(Collectors.toList());
    }



    @Override
    public List<Palpite> findByPartidaIdInAndUsuarioId(List<Integer> partidaIds, Integer usuarioId) {
        return repository.findByPartidaIdInAndUsuarioId(partidaIds, usuarioId).stream()
                .map(entity -> modelMapper.map(entity, Palpite.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UsuarioPontuacaoAggregate> sumPontuacaoPorUsuarioEmPartidasFinalizadas() {
        return repository.sumPontuacaoPorUsuarioEmPartidasFinalizadas().stream()
                .map(row -> UsuarioPontuacaoAggregate.builder()
                        .usuarioId(row.getUsuarioId().longValue())
                        .pontuacaoTotal(row.getPontuacaoTotal().intValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Page<Palpite> findByUsuarioIdAndCampeonatoIdAndFaseIdPaged(
            Integer usuarioId,
            Integer campeonatoId,
            Integer faseId,
            int page,
            int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return repository
                .findByUsuarioIdAndCampeonatoIdAndFaseId(usuarioId, campeonatoId, faseId, pageRequest)
                .map(entity -> modelMapper.map(entity, Palpite.class));
    }
}
