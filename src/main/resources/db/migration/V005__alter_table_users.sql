ALTER TABLE messages.Users_tb
    ADD CONSTRAINT fk_foto_perfil FOREIGN KEY (foto_perfil_id) REFERENCES messages.arquivo_tb (id);

ALTER TABLE messages.Users_tb
    ADD CONSTRAINT fk_foto_capa FOREIGN KEY (foto_capa_id) REFERENCES messages.arquivo_tb (id);
