package org.campus.connect.message.links;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LinksRepository extends JpaRepository<Links, Long> {
  List<Links> findAllByExcluded(Boolean excluded);

  @Query("SELECT l FROM Links l WHERE l.id_msg = :msgId")
  List<Links> findAllById_msg(@Param("msgId") Long msgId);
}
