package org.campus.connect.message.message.view;

import org.campus.connect.message.course.CourseDTO;
import org.campus.connect.message.course.CourseService;
import org.campus.connect.message.message.MessageDTO;
import org.campus.connect.message.message.MessageService;
import org.campus.connect.message.message.mobile.MsgViewDTO;
import org.campus.connect.message.users.UsersDTO;
import org.campus.connect.message.users.UsersService;
import org.campus.connect.message.utils.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ViewServiceImpl extends GenericServiceImpl<View, ViewDTO> implements ViewService {

  private final ViewRepository repository;
  private final ViewMapper mapper;
  private final UsersService usersService;
  private final MessageService messageService;
  private final CourseService courseService;

  public ViewServiceImpl(
    final ViewRepository repository, final ViewMapper mapper,
    final UsersService usersService, final MessageService messageService,
    final CourseService courseService) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
    this.usersService = usersService;
    this.messageService = messageService;
    this.courseService = courseService;
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

  @Override
  public List<ViewDTO> getByIdMsg(final UUID id) {
    return this.mapper.toDto(this.repository.findAllByMessage(id))
      .stream()
      .peek(viewDTO -> {
        UsersDTO usersDTO = viewDTO.getUser();
        if (usersDTO.getId_curso() != null) {
          CourseDTO course = courseService.getMinimalById(usersDTO.getId_curso());
          viewDTO.setCourse(course.getName());
        }
      })
      .collect(Collectors.toList());
  }

  @Override
  public ViewDTO setView(MsgViewDTO vw) {
    return repository.findByIdUserAndIdMessage(vw.getUser(), vw.getMessage())
      .map(obj -> {
        obj.setViewed(vw.getView());
        obj.setFavorite(vw.getFavorite());
        obj.setReceived(true);
        try {
          return this.save(mapper.toDto(obj));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      })
      .orElseGet(() -> {
        UsersDTO user = usersService.getUserById(vw.getUser());
        MessageDTO msg = messageService.findMsgById(vw.getMessage());
        ViewDTO view = new ViewDTO();
        view.setUser(user);
        view.setMessage(msg);
        view.setViewed(vw.getView());
        view.setFavorite(vw.getFavorite());
        view.setViewDate(LocalDateTime.now());
        view.setReceived(true);
        try {
          return this.save(view);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
  }
}
