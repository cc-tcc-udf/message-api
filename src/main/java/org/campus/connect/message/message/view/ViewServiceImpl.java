package org.campus.connect.message.message.view;

import org.campus.connect.message.message.MessageDTO;
import org.campus.connect.message.message.MessageService;
import org.campus.connect.message.users.UsersDTO;
import org.campus.connect.message.users.UsersService;
import org.campus.connect.message.utils.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ViewServiceImpl extends GenericServiceImpl<View, ViewDTO> implements ViewService {

  private final ViewRepository repository;
  private final ViewMapper mapper;
  private final UsersService usersService;
  private final MessageService messageService;

  public ViewServiceImpl(
    final ViewRepository repository, final ViewMapper mapper,
    final UsersService usersService, final MessageService messageService) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
    this.usersService = usersService;
    this.messageService = messageService;
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
  }  @Override
  public List<ViewDTO> getByIdMsg(final UUID id) {
    return this.mapper.toDto(this.repository.findAllByMessage(id));
  }

  @Override
  public ViewDTO setView(UUID idUser, UUID idMessage) throws Exception {
    UsersDTO user = this.usersService.getUserById(idUser);
    MessageDTO msg = this.messageService.findMsgById(idMessage);
    if (user == null || msg == null) {
      return null;
    }

    ViewDTO dto = new ViewDTO();
    dto.setViewed(true);
    dto.setReceived(true);
    dto.setViewDate(LocalDateTime.now());
    dto.setUser(user);
    dto.setMessage(msg);
    return this.save(dto);
  }
}
