CREATE TABLE IF NOT EXISTS messages.Links_tb
(
    id           SERIAL PRIMARY KEY,
    excluded     BOOLEAN   DEFAULT FALSE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(255),
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by   VARCHAR(255),
    title       VARCHAR(255),
    link         VARCHAR(255),
    id_msg       BIGINT
)