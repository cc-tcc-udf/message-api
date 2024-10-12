package org.campus.connect.message.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

  List<Message> findAllByExcluded(Boolean excluded);
}
