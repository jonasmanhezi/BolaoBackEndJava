package com.bolao.v1.infrastructure.supabase.time;

import com.bolao.v1.infrastructure.model.TimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRepository extends JpaRepository<TimeEntity, Integer> {
}
