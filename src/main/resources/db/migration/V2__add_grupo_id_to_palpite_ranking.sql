ALTER TABLE tb_palpite
    ADD COLUMN IF NOT EXISTS grupo_id BIGINT REFERENCES tb_grupos (id);

ALTER TABLE tb_ranking
    ADD COLUMN IF NOT EXISTS grupo_id BIGINT REFERENCES tb_grupos (id);

CREATE UNIQUE INDEX IF NOT EXISTS uq_palpite_usuario_partida_grupo
    ON tb_palpite (usuario_id, partida_id, grupo_id)
    WHERE grupo_id IS NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_ranking_user_grupo
    ON tb_ranking (user_id, grupo_id)
    WHERE grupo_id IS NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_grupo_membro
    ON tb_grupo_membro (grupo_id, usuario_id);