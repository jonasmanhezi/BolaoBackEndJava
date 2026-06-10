package com.bolao.v1.infrastructure.supabase.campeonato;

import com.bolao.v1.infrastructure.model.CampeonatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampeonatoRepository extends JpaRepository<CampeonatoEntity, Integer> {
}