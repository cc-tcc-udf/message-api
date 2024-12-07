package org.campus.connect.message.message.view;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ViewRepository extends JpaRepository<View, UUID> {
  List<View> findAllByExcluded(Boolean excluded);

  @Query("SELECT v FROM View v WHERE v.user.id = :id")
  List<View> findAllByUser(@Param("id") UUID id);

  @Query("SELECT v FROM View v WHERE v.message.id = :id")
  List<View> findAllByMessage(@Param("id") UUID id);

//  @Query("SELECT v FROM View v WHERE v.message.id = :idMsg and v.user.id = :idUser")
//  View findByIdUserAndIdMessage(@Param("idUser") UUID idUser, @Param("idMsg") UUID idMessage);

  @Query("SELECT v FROM View v WHERE v.message.id = :idMsg and v.user.id = :idUser")
  Optional<View> findByIdUserAndIdMessage(@Param("idUser") UUID idUser, @Param("idMsg") UUID idMessage);
}
