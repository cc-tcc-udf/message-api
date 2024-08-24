package org.campus.connect.message.files;

import org.campus.connect.message.links.Links;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {

  List<ArquivoDTO> findAllByExcluded(Boolean excluded);


  @Query("SELECT a FROM Arquivo a WHERE a.id_ext = :idExt")
  List<Arquivo> findAllById_ext(@Param("idExt") Long id_ext);
}
