package org.campus.connect.message.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findAllByExcluded(Boolean excluded);

  @Query(value = "SELECT * FROM messages.message_tb", nativeQuery = true)
  List<Message> findMessages();

  List<Message> findAllByResponsible(UUID responsible);

  @Query(
    "SELECT m FROM Message m JOIN m.courses c WHERE c.id = :courseId"
  )
  List<Message> findAllByCourseId(@Param("courseId") UUID courseId);

}
