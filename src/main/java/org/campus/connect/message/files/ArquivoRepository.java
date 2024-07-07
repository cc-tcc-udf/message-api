package org.campus.connect.message.files;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {

  List<ArquivoDTO> findAllByExcluded(Boolean excluded);

}
