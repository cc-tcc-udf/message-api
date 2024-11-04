ALTER TABLE messages.course_tb
    ALTER COLUMN created_by TYPE TEXT USING created_by::TEXT,
    ALTER COLUMN updated_by TYPE TEXT USING updated_by::TEXT;

ALTER TABLE messages.file_tb
    ALTER COLUMN created_by TYPE TEXT USING created_by::TEXT,
    ALTER COLUMN updated_by TYPE TEXT USING updated_by::TEXT;

ALTER TABLE messages.links_tb
    ALTER COLUMN created_by TYPE TEXT USING created_by::TEXT,
    ALTER COLUMN updated_by TYPE TEXT USING updated_by::TEXT;

ALTER TABLE messages.message_tb
    ALTER COLUMN created_by TYPE TEXT USING created_by::TEXT,
    ALTER COLUMN updated_by TYPE TEXT USING updated_by::TEXT;

ALTER TABLE messages.users_tb
    ALTER COLUMN created_by TYPE TEXT USING created_by::TEXT,
    ALTER COLUMN updated_by TYPE TEXT USING updated_by::TEXT;