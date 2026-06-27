package com.bolao.v1.core.port.out.campeonato;

import com.bolao.v1.core.domain.entity.campeonato.Campeonato;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CampeonatoRepositoryPortOut {


    Campeonato save(Campeonato campeonato);

    Optional<Campeonato> findById(Integer id);

    List<Campeonato> findAll();

    boolean existsById(Integer id);

    void deleteById (Integer id);



}
