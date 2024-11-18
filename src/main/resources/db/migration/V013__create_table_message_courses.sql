CREATE TABLE IF NOT EXISTS messages.Message_courses_tb
(
    message_id UUID NOT NULL,
    course_id  UUID NOT NULL,
    PRIMARY KEY (message_id, course_id),
    FOREIGN KEY (message_id) REFERENCES messages.Message_tb (id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES messages.Course_tb (id) ON DELETE CASCADE
);