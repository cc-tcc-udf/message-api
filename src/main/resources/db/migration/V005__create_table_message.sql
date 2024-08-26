CREATE TABLE IF NOT EXISTS messages.Message_tb
(
    id           SERIAL PRIMARY KEY,
    excluded     BOOLEAN   DEFAULT FALSE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(255),
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by   VARCHAR(255),
    titulo       VARCHAR(255),
    resumo       VARCHAR(255),
    data_envio   TIMESTAMP,
    status       VARCHAR(50),
    message      TEXT,
    resp UUID
);