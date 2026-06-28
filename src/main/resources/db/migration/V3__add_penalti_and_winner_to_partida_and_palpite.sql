ALTER TABLE tb_partida
    ADD COLUMN tem_penalti        BOOLEAN     NOT NULL DEFAULT FALSE,
    ADD COLUMN winner_id          INTEGER,
    ADD COLUMN penalti_casa       INTEGER,
    ADD COLUMN penalti_visitante  INTEGER;

ALTER TABLE tb_palpite
    ADD COLUMN palpite_winner_id  INTEGER;
