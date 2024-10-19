-- Migration to create the Users_tb table
CREATE TABLE IF NOT EXISTS messages.Users_tb
(
    id             SERIAL PRIMARY KEY,
    uid UUID DEFAULT gen_random_uuid() NOT NULL,
    name           VARCHAR(255) NOT NULL,
    email          VARCHAR(255) NOT NULL,
    password       VARCHAR(255) NOT NULL,
    phone          VARCHAR(255) NOT NULL,
    profile_photo_id BIGINT,
    cover_photo_id   BIGINT,
    id_curso   BIGINT,
    excluded       BOOLEAN   DEFAULT FALSE,
    created_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by     VARCHAR(255),
    updated_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by     VARCHAR(255),
    CONSTRAINT fk_profile_photo FOREIGN KEY (profile_photo_id) REFERENCES messages.file_tb (id),
    CONSTRAINT fk_cover_photo FOREIGN KEY (cover_photo_id) REFERENCES messages.file_tb (id)
);

-- Migration to create the auxiliary table user_permissions
CREATE TABLE IF NOT EXISTS messages.user_permissions
(
    user_id BIGINT       NOT NULL,
    roles   VARCHAR(255) NOT NULL,
    CONSTRAINT user_permissions_pk PRIMARY KEY (user_id, roles),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES messages.Users_tb (id)
);
