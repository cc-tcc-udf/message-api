CREATE TABLE IF NOT EXISTS messages.Links_tb
(
    id            UUID PRIMARY KEY,
    excluded     BOOLEAN   DEFAULT FALSE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by   TEXT,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by   TEXT,
    title       VARCHAR(255),
    link         VARCHAR(255),
    id_msg       UUID
)