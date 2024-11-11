package org.campus.connect.message.files;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {

  List<FileDTO> findAllByExcluded(Boolean excluded);

  @Query("SELECT a FROM File a WHERE a.id_ext = :idExt")
  List<File> findAllById_ext(@Param("idExt") UUID id_ext);

  @Query("SELECT a FROM File a WHERE a.id_ext = :idExt")
  File findById_ext(@Param("idExt") UUID id_ext);
}
