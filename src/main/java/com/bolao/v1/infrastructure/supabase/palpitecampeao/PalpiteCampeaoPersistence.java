package com.bolao.v1.infrastructure.supabase.palpitecampeao;

import com.bolao.v1.core.domain.entity.palpitecampeao.PalpiteCampeao;
import com.bolao.v1.core.port.out.palpitecampeao.PalpiteCampeaoRepositoryPortOut;
import com.bolao.v1.infrastructure.model.PalpiteCampeaoEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PalpiteCampeaoPersistence implements PalpiteCampeaoRepositoryPortOut {

    private final PalpiteCampeaoRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public PalpiteCampeao save(PalpiteCampeao palpiteCampeao) {
        PalpiteCampeaoEntity entity = modelMapper.map(palpiteCampeao, PalpiteCampeaoEntity.class);
        PalpiteCampeaoEntity saved = repository.save(entity);
        return modelMapper.map(saved, PalpiteCampeao.class);
    }

    @Override
    public boolean existsByUsuarioId(Long usuarioId) {
        return repository.existsByUsuarioId(usuarioId);
    }

    @Override
    public Optional<PalpiteCampeao> findByUsuarioId(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId)
                .map(entity -> modelMapper.map(entity, PalpiteCampeao.class));
    }
}
