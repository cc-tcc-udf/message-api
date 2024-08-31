CREATE TABLE IF NOT EXISTS messages.Message_tb
(
    id           SERIAL PRIMARY KEY,
    excluded     BOOLEAN   DEFAULT FALSE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(255),
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by   VARCHAR(255),
    title        VARCHAR(255),
    summary      VARCHAR(255),
    send_date    TIMESTAMP,
    status       VARCHAR(50),
    message      TEXT,
    response     UUID
);
