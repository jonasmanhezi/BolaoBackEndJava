package com.bolao.v1.infrastructure.supabase.partida;

import com.bolao.v1.core.domain.entity.partida.Partida;
import com.bolao.v1.infrastructure.model.CampeonatoEntity;
import com.bolao.v1.infrastructure.model.PartidaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<PartidaEntity, Integer> {
    List<PartidaEntity> findByFaseIdAndCampeonatoId(Integer faseId, Integer campeonatoId);

    @org.springframework.data.jpa.repository.Query("""
            SELECT p FROM PartidaEntity p
            LEFT JOIN FETCH p.timeCasa
            LEFT JOIN FETCH p.timeVisitante
            WHERE p.status = :status
            """)
    List<PartidaEntity> findByStatusWithTimes(Partida.StatusPartida status);

    @org.springframework.data.jpa.repository.Query("""
            SELECT p FROM PartidaEntity p
            LEFT JOIN FETCH p.timeCasa
            LEFT JOIN FETCH p.timeVisitante
            WHERE p.dataHoraPartida BETWEEN :inicio AND :fim
            """)
    List<PartidaEntity> findByDataHoraPartidaBetweenWithTimes(Instant inicio, Instant fim);

    @org.springframework.data.jpa.repository.Query("SELECT p FROM PartidaEntity p LEFT JOIN FETCH p.timeCasa LEFT JOIN FETCH p.timeVisitante")
    List<PartidaEntity> findAllWithTimes();

    @org.springframework.data.jpa.repository.Query("SELECT p FROM PartidaEntity p LEFT JOIN FETCH p.timeCasa LEFT JOIN FETCH p.timeVisitante WHERE p.id = :id")
    java.util.Optional<PartidaEntity> findByIdWithTimes(Integer id);

    @org.springframework.data.jpa.repository.Query("SELECT p FROM PartidaEntity p LEFT JOIN FETCH p.timeCasa LEFT JOIN FETCH p.timeVisitante WHERE p.faseId = :faseId AND p.campeonatoId = :campeonatoId")
    List<PartidaEntity> findByFaseIdAndCampeonatoIdWithTimes(Integer faseId, Integer campeonatoId);

}
