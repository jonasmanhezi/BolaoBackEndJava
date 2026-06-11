package com.bolao.v1.infrastructure.supabase.partida;

import com.bolao.v1.core.domain.entity.campeonato.Campeonato;
import com.bolao.v1.core.domain.entity.partida.Partida;
import com.bolao.v1.core.port.out.partida.PartidaRepositoryPortOut;

import com.bolao.v1.infrastructure.model.CampeonatoEntity;
import com.bolao.v1.infrastructure.model.PartidaEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PartidaPersistence implements PartidaRepositoryPortOut {

    private final PartidaRepository repository;
    private final ModelMapper mapper;


    public Optional<Partida> findById(Integer id) {

        return repository.findByIdWithTimes(id)
                .map(this::toDomainWithLogos);
    }

    @Override
    public List<Partida> findAll() {
        return repository.findAllWithTimes().stream()
                .map(this::toDomainWithLogos)
                .toList();
    }

    @Override
    public List<Partida> findByFaseId(Integer faseId, Integer campeonatoId) {
        return repository.findByFaseIdAndCampeonatoIdWithTimes(faseId, campeonatoId).stream()
                .map(this::toDomainWithLogos)
                .toList();
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }




    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }


    @Override
    public Partida save (Partida entity) {

        PartidaEntity jpaEntity = mapper.map(entity, PartidaEntity.class);

        PartidaEntity saved = repository.save(jpaEntity);

        return mapper.map(saved, Partida.class);




    }

    private Partida toDomainWithLogos(PartidaEntity entity) {
        Partida partida = new Partida();
        partida.setId(entity.getId());
        partida.setCampeonatoId(entity.getCampeonatoId());
        partida.setFaseId(entity.getFaseId());
        partida.setTimeCasaId(entity.getTimeCasaId());
        partida.setTimeVisitanteId(entity.getTimeVisitanteId());
        partida.setDataHoraPartida(entity.getDataHoraPartida());
        partida.setGolsCasa(entity.getGolsCasa());
        partida.setGolsVisitante(entity.getGolsVisitante());
        partida.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        partida.setExternalId(entity.getExternalId());

        if (entity.getTimeCasa() != null) {
            partida.setNomeCasa(entity.getTimeCasa().getNome());
            partida.setLogoCasa(entity.getTimeCasa().getLogo());
        }
        if (entity.getTimeVisitante() != null) {
            partida.setNomeVisitante(entity.getTimeVisitante().getNome());
            partida.setLogoFora(entity.getTimeVisitante().getLogo());
        }

        return partida;
    }

    @Override
    public List<Partida> findByCampeonatoIdAndFaseId(Integer campeonatoId, Integer faseId) {
        return repository.findByFaseIdAndCampeonatoIdWithTimes(campeonatoId, faseId).stream()
                .map(this::toDomainWithLogos)
                .collect(Collectors.toList());
    }

    @Override
    public List<Partida> findByStatus(Partida.StatusPartida status) {
        return repository.findByStatus(status).stream()
                .map(entity -> mapper.map(entity, Partida.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Partida> findPartidasDeHoje(Instant inicio, Instant fim) {
        return repository.findByDataHoraPartidaBetween(inicio, fim).stream()
                .map(entity -> mapper.map(entity, Partida.class))
                .collect(Collectors.toList());
    }

}
