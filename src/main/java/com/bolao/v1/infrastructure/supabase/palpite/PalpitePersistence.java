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
    public List<Palpite> findByPartidaIdInAndUsuarioIdAndGrupoId(
            List<Integer> partidaIds,
            Integer usuarioId,
            Long grupoId
    ) {
        return repository.findByPartidaIdInAndUsuarioIdAndGrupoId(partidaIds, usuarioId, grupoId).stream()
                .map(entity -> modelMapper.map(entity, Palpite.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByPartidaIdAndUsuarioIdAndGrupoId(Integer partidaId, Integer usuarioId, Long grupoId) {
        return repository.existsByPartidaIdAndUsuarioIdAndGrupoId(partidaId, usuarioId, grupoId);
    }

    @Override
    public List<UsuarioPontuacaoAggregate> sumPontuacaoPorUsuarioEmPartidasFinalizadasPorGrupo(Long grupoId) {
        return repository.sumPontuacaoPorUsuarioEmPartidasFinalizadasPorGrupo(grupoId).stream()
                .map(row -> UsuarioPontuacaoAggregate.builder()
                        .usuarioId(row.getUsuarioId().longValue())
                        .pontuacaoTotal(row.getPontuacaoTotal().intValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<Palpite> findByUsuarioIdAndGrupoId(Integer usuarioId, Long grupoId) {
        return repository.findByUsuarioIdAndGrupoIdOrderByIdAsc(usuarioId, grupoId).stream()
                .map(entity -> modelMapper.map(entity, Palpite.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<Palpite> findByUsuarioIdAndGrupoIdPaged(
            Integer usuarioId,
            Long grupoId,
            int page,
            int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return repository
                .findByUsuarioIdAndGrupoIdOrderByIdAsc(usuarioId, grupoId, pageRequest)
                .map(entity -> modelMapper.map(entity, Palpite.class));
    }
}