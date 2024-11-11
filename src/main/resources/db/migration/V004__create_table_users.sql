CREATE TABLE IF NOT EXISTS messages.Users_tb
(
    id             UUID PRIMARY KEY ,
    name           VARCHAR(255) NOT NULL,
    email          VARCHAR(255) NOT NULL,
    password       VARCHAR(255) NOT NULL,
    phone          VARCHAR(255) NOT NULL,
    profile_photo_id UUID,
    cover_photo_id   UUID,
    id_curso   UUID,
    excluded       BOOLEAN   DEFAULT FALSE,
    created_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by   TEXT,
    updated_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by     TEXT,
    CONSTRAINT fk_profile_photo FOREIGN KEY (profile_photo_id) REFERENCES messages.file_tb (id),
    CONSTRAINT fk_cover_photo FOREIGN KEY (cover_photo_id) REFERENCES messages.file_tb (id)
);

-- Migration to create the auxiliary table user_permissions
CREATE TABLE IF NOT EXISTS messages.user_permissions
(
    user_id UUID       NOT NULL,
    roles   VARCHAR(255) NOT NULL,
    CONSTRAINT user_permissions_pk PRIMARY KEY (user_id, roles),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES messages.Users_tb (id)
);
