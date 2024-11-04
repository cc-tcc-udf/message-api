CREATE TABLE IF NOT EXISTS messages.user_tokens
(
    user_id BIGINT       NOT NULL,
    token   VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, token),
    FOREIGN KEY (user_id) REFERENCES messages.Users_tb (id)
);
