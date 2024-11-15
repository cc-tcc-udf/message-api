package org.campus.connect.message.message.view;

import org.campus.connect.message.links.Links;
import org.campus.connect.message.links.LinksDTO;
import org.campus.connect.message.links.LinksMapper;
import org.campus.connect.message.links.LinksRepository;
import org.campus.connect.message.links.LinksService;
import org.campus.connect.message.utils.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ViewServiceImpl extends GenericServiceImpl<View, ViewDTO> implements ViewService {

  private final ViewRepository repository;
  private final ViewMapper mapper;

  public ViewServiceImpl(
    final ViewRepository repository,
    final ViewMapper mapper) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public List<ViewDTO> findAll() {
    List<View> list = this.repository.findAll();
    return this.mapper.toDto(list);
  }

  @Override
  public ViewDTO create(final ViewDTO dto) throws Exception {
    return this.save(dto);
  }

  @Override
  public List<ViewDTO> findByIdUser(final UUID id) {
    return this.mapper.toDto(this.repository.findAllByUser(id));
  }

}
