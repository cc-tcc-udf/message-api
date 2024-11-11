CREATE TABLE IF NOT EXISTS MESSAGES.Audit_tb
(
    id           UUID PRIMARY KEY,
    UPDATED_DATE TIMESTAMPTZ NOT NULL,
    user_infos   TEXT,
    entity_name  VARCHAR(255),
    changed_data TEXT,
    operation    VARCHAR(50),
    entity_id    UUID
);

