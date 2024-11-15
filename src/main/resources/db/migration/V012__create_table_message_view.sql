CREATE TABLE IF NOT EXISTS messages.Message_view_tb
(
    id            UUID PRIMARY KEY,
    excluded     BOOLEAN   DEFAULT FALSE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by   TEXT,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by   TEXT,
    message_id    UUID NOT NULL,
    user_id       UUID NOT NULL,
    view_date     TIMESTAMP,
    viewed     BOOLEAN DEFAULT FALSE,
    received_date TIMESTAMP,
    received      BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_message FOREIGN KEY (message_id) REFERENCES messages.Message_tb (id) ON DELETE CASCADE,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES messages.users_tb (id) ON DELETE CASCADE
);
