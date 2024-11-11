package org.campus.connect.message.links;

import org.campus.connect.message.utils.GenericService;

import java.util.List;
import java.util.UUID;

public interface LinksService extends GenericService<LinksDTO> {

  List<LinksDTO> findAll();

  LinksDTO create(LinksDTO linksDTO) throws Exception;

  List<LinksDTO> findByIdMsg(UUID id);
}
