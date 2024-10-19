ALTER TABLE messages.Message_tb
    ADD CONSTRAINT fk_message_course
        FOREIGN KEY (course_id)
            REFERENCES messages.Course_tb (id)
            ON DELETE SET NULL;

-- Create the message_links join table
CREATE TABLE IF NOT EXISTS messages.message_links_tb
(
    message_id BIGINT NOT NULL,
    link_id    BIGINT NOT NULL,
    PRIMARY KEY (message_id, link_id),
    CONSTRAINT fk_message FOREIGN KEY (message_id) REFERENCES messages.message_tb (id) ON DELETE CASCADE,
    CONSTRAINT fk_link FOREIGN KEY (link_id) REFERENCES messages.links_tb (id) ON DELETE CASCADE
);

-- Create the message_attachments join table
CREATE TABLE IF NOT EXISTS messages.message_attachments_tb
(
    message_id BIGINT NOT NULL,
    file_id    BIGINT NOT NULL,
    PRIMARY KEY (message_id, file_id),
    CONSTRAINT fk_message_attachment FOREIGN KEY (message_id) REFERENCES messages.message_tb (id) ON DELETE CASCADE,
    CONSTRAINT fk_file FOREIGN KEY (file_id) REFERENCES messages.file_tb (id) ON DELETE CASCADE
);