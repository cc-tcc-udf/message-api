CREATE TABLE IF NOT EXISTS messages.Message_tb
(
    id             UUID PRIMARY KEY,
    excluded       BOOLEAN   DEFAULT FALSE,
    created_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by     TEXT,
    updated_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by     TEXT,
    title          VARCHAR(255),
    summary        VARCHAR(255),
    send_date      TIMESTAMP,
    status         VARCHAR(50),
    message        TEXT,
    course_id      UUID,
    responsible_id UUID
);
