package com.bolao.v1.infrastructure.supabase.ranking;

public interface RankingComNomeProjection {

    Long getUserId();

    Short getPontuacao();

    String getNome();
}