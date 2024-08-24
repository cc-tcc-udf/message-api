package org.campus.connect.message.links;

import org.campus.connect.message.utils.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinksServiceImpl extends GenericServiceImpl<Links, LinksDTO> implements LinksService {

  private final LinksRepository repository;
  private final LinksMapper mapper;

  public LinksServiceImpl(
    final LinksRepository repository,
    final LinksMapper mapper) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public List<LinksDTO> findAll() {
    List<Links> list = this.repository.findAll();
    return this.mapper.toDto(list);
  }

  @Override
  public LinksDTO create(final LinksDTO linksDTO) throws Exception {
    return this.save(linksDTO);
  }

  @Override
  public List<LinksDTO> findByIdMsg(final Long id) {
    return this.mapper.toDto(this.repository.findAllById_msg(id));
  }

}
