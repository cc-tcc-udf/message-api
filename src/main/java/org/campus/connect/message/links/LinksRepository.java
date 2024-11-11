package org.campus.connect.message.links;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LinksRepository extends JpaRepository<Links, UUID> {
  List<Links> findAllByExcluded(Boolean excluded);

  @Query("SELECT l FROM Links l WHERE l.id_msg = :msgId")
  List<Links> findAllById_msg(@Param("msgId") UUID msgId);
}
