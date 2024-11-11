CREATE TABLE IF NOT EXISTS messages.user_tokens
(
    user_id uuid       NOT NULL,
    token  TEXT NOT NULL,
    PRIMARY KEY (user_id, token),
    FOREIGN KEY (user_id) REFERENCES messages.Users_tb (id)
);
