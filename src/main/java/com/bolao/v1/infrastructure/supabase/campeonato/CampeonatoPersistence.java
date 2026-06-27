package com.bolao.v1.infrastructure.supabase.campeonato;


import com.bolao.v1.core.domain.entity.campeonato.Campeonato;
import com.bolao.v1.core.port.out.campeonato.CampeonatoRepositoryPortOut;
import com.bolao.v1.infrastructure.model.CampeonatoEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CampeonatoPersistence implements CampeonatoRepositoryPortOut {


    private final CampeonatoRepository repository;
    private final ModelMapper mapper;


    @Override
    public Campeonato save (Campeonato entity) {

        CampeonatoEntity jpaEntity = mapper.map(entity, CampeonatoEntity.class);

        CampeonatoEntity saved = repository.save(jpaEntity);

        return mapper.map(saved, Campeonato.class);


    }

    @Override

    public Optional<Campeonato> findById(Integer id) {

        return repository.findById(id)
                .map(jpaEntity -> mapper.map(jpaEntity, Campeonato.class));
    }


    @Override
    public List<Campeonato> findAll() {
        return repository.findAll().stream()
                .map(jpaEntity -> mapper.map(jpaEntity, Campeonato.class))
                .toList();
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }


}
