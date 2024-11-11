CREATE TABLE IF NOT EXISTS messages.Course_tb
(
    id             UUID PRIMARY KEY,
    excluded     BOOLEAN   DEFAULT FALSE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by   TEXT,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by   TEXT,
    name         VARCHAR(255),
    description  TEXT,
    abbreviation VARCHAR(255),
    resp UUID,
    course_group_id UUID,
    is_group BOOLEAN NOT NULL DEFAULT FALSE
)