package org.campus.connect.message.files;


import org.campus.connect.message.links.LinksDTO;
import org.campus.connect.message.utils.GenericService;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArquivoService extends GenericService<ArquivoDTO> {

  List<ArquivoDTO> findAll();

  ArquivoDTO create(MultipartFile file) throws Exception;

  List<ArquivoDTO> findByIdExt(Long id);

  Resource getFile(Long id) throws Exception;

  ArquivoDTO update(Long id, MultipartFile file) throws Exception;

}
