package org.campus.connect.message.files;


import org.campus.connect.message.utils.GenericService;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface FileService extends GenericService<FileDTO> {

  List<FileDTO> findAll();

  FileDTO create(MultipartFile file) throws Exception;

  List<FileDTO> findAllByIdExt(UUID id);

  FileDTO findByIdExt(UUID id);

  Resource getFile(UUID id) throws Exception;

  FileDTO update(UUID id, MultipartFile file) throws Exception;

}
