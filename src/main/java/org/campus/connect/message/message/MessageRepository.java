package org.campus.connect.message.message;

import org.campus.connect.message.constants.Enums.UserRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findAll(Specification<Message> specification);

  Page<Message> findAll(Specification<Message> specification, Pageable springPageable);

  List<Message> findAllByExcluded(Boolean excluded);

  @Query(value = "SELECT * FROM messages.message_tb", nativeQuery = true)
  List<Message> findMessages();

  List<Message> findAllByResponsible(UUID responsible);

  @Query(
    "SELECT m FROM Message m JOIN m.courses c WHERE c.id = :courseId"
  )
  List<Message> findAllByCourseId(@Param("courseId") UUID courseId);

  @Query(
    "SELECT m FROM Message m JOIN m.courses c" +
      " WHERE c.id = :courseId AND m.status = 'ENVIADO'" +
      "ORDER BY m.sendDate DESC"
  )
  List<Message> findAllByCourseIdAndStatusEnviado(@Param("courseId") UUID courseId);

  @Query("SELECT COUNT(v) FROM View v WHERE v.message.id = :id")
  Number getLength(@Param("id") UUID id);

  @Query("SELECT COUNT(u) FROM Users u JOIN u.roles r WHERE u.id_curso = :id AND r = :role")
  Number getAlunos(@Param("id") UUID id, @Param("role") UserRoles role);

  @Query(
    "SELECT m FROM Message m JOIN m.courses c " +
      "WHERE c.id = :courseId AND m.status = 'ENVIADO' " +
      "ORDER BY m.sendDate DESC"
  )
  Page<Message> findByCourseIdAndStatusEnviado(@Param("courseId") UUID courseId, Pageable pageable);

  @Query(
    "SELECT m FROM Message m " +
      "WHERE m.responsible = :respId AND m.status = 'ENVIADO' " +
      "ORDER BY m.sendDate DESC"
  )
  Page<Message> findByResponsibleAndStatusEnviado(@Param("respId") UUID respId, Pageable pageable);

}
