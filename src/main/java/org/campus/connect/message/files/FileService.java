package org.campus.connect.message.files;


import org.campus.connect.message.utils.GenericService;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService extends GenericService<FileDTO> {

  List<FileDTO> findAll();

  FileDTO create(MultipartFile file) throws Exception;

  List<FileDTO> findAllByIdExt(Long id);

  FileDTO findByIdExt(Long id);

  Resource getFile(Long id) throws Exception;

  FileDTO update(Long id, MultipartFile file) throws Exception;

}
