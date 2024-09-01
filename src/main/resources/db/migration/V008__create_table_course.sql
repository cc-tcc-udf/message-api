CREATE TABLE IF NOT EXISTS messages.Course_tb
(
    id           SERIAL PRIMARY KEY,
    excluded     BOOLEAN   DEFAULT FALSE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(255),
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by   VARCHAR(255),
    name         VARCHAR(255),
    description  TEXT,
    abbreviation VARCHAR(255),
    resp BIGINT,
    course_group_id BIGINT,
    is_group BOOLEAN NOT NULL DEFAULT FALSE
)