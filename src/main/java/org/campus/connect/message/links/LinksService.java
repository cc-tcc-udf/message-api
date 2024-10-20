package org.campus.connect.message.links;

import org.campus.connect.message.utils.GenericService;

import java.util.List;

public interface LinksService extends GenericService<LinksDTO> {

  List<LinksDTO> findAll();

  LinksDTO create(LinksDTO linksDTO) throws Exception;

  List<LinksDTO> findByIdMsg(Long id);
}
