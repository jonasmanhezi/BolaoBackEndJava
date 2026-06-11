package com.bolao.v1.core.port.out.partida;


import com.bolao.v1.core.domain.entity.partida.Partida;


import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PartidaRepositoryPortOut {


    Optional<Partida> findById(Integer id);

    List<Partida> findAll();

    List<Partida> findByFaseId(Integer faseId, Integer campeonatoId);

    Partida save(Partida partida);

    void deleteById (Integer id);

    boolean existsById(Integer id);

    List<Partida> findByCampeonatoIdAndFaseId(Integer campeonatoId, Integer faseId);

    List<Partida> findByStatus(Partida.StatusPartida status);

    List<Partida> findPartidasDeHoje(Instant inicio, Instant fim);
}
