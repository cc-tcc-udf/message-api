ALTER TABLE messages.users_tb
    ADD COLUMN active boolean default false;
ALTER TABLE messages.users_tb
    ADD COLUMN temporary_password varchar(50);