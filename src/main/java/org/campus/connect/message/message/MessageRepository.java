package org.campus.connect.message.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findAllByExcluded(Boolean excluded);

  @Query(value = "SELECT * FROM messages.message_tb", nativeQuery = true)
  List<Message> findMessages();

}
