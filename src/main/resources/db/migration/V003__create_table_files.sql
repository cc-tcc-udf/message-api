CREATE TABLE IF NOT EXISTS messages.file_tb
(
    id            UUID PRIMARY KEY,
    excluded     BOOLEAN   DEFAULT FALSE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by   TEXT,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by   TEXT,
    key          VARCHAR(255),
    name         VARCHAR(255),
    size         BIGINT,
    type         VARCHAR(255),
    url          VARCHAR(255),
    id_ext       UUID
);