CREATE TABLE IF NOT EXISTS messages.file_tb
(
    id           SERIAL PRIMARY KEY,
    excluded     BOOLEAN   DEFAULT FALSE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(255),
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by   VARCHAR(255),
    uid          UUID      DEFAULT gen_random_uuid(),
    key          VARCHAR(255),
    name         VARCHAR(255),
    size         BIGINT,
    type         VARCHAR(255),
    url          VARCHAR(255),
    id_ext       BIGINT
);