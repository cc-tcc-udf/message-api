package org.campus.connect.message.message;

import org.campus.connect.message.constants.Enums.Status;
import org.campus.connect.message.course.CourseDTO;
import org.campus.connect.message.course.CourseService;
import org.campus.connect.message.files.FileService;
import org.campus.connect.message.firebase.FirebaseMessageDTO;
import org.campus.connect.message.firebase.FirebaseService;
import org.campus.connect.message.links.LinksDTO;
import org.campus.connect.message.links.LinksService;
import org.campus.connect.message.utils.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class MessageServiceImpl extends GenericServiceImpl<Message, MessageDTO> implements MessageService {

  private final MessageRepository repository;
  private final MessageMapper mapper;
  private final FileService fileService;
  private final LinksService linksService;
  private final FirebaseService firebaseService;
  private final CourseService courseService;

  public MessageServiceImpl(
    final MessageRepository repository,
    final MessageMapper mapper, final FileService fileService,
    final LinksService linksService, final FirebaseService firebaseService,
    final CourseService courseService) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
    this.fileService = fileService;
    this.linksService = linksService;
    this.firebaseService = firebaseService;
    this.courseService = courseService;
  }

  @Override
  public MessageDTO findMsgById(final UUID id) {
    Message message = repository.findById(id).orElse(null);
    if (message == null) {
      return null;
    }
    return mapper.toDto(message);
  }

  @Override
  public List<MessageDTO> findAll() {
    List<Message> messages = repository.findMessages();
    return this.mapper.toDto(messages);
  }

  @Override
  public MessageDTO create(final MessageDTO msg) throws Exception {
    if (msg.getLinks() != null && !msg.getLinks().isEmpty()) {
      msg.getLinks().forEach(link -> {
        try {
          LinksDTO savedLink = this.linksService.create(link);
          link.setId(savedLink.getId());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    }
    return this.save(msg);
  }

  public MessageDTO send(final MessageDTO msg) throws Exception {
    if (msg.getCourse() == null) {
      return null;
    }
    msg.setSendDate(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.of("-03:00")));
    msg.setStatus(Status.ENVIADO);
    this.firebaseSend(msg);
    return this.save(msg);
  }

  private void firebaseSend(final MessageDTO msg) throws ExecutionException, InterruptedException {
    CourseDTO group = courseService.findById(msg.getCourse().getCourseGroupId());
    String course = msg.getCourse().getName();
    List<FirebaseMessageDTO> tokens = firebaseService.fetchTokens(course, group.getName());
    tokens.forEach(token -> {
      firebaseService.sendNotification(FirebaseMessageDTO.builder()
        .notificationToken(token.getNotificationToken())
        .title(msg.getTitle())
        .body(msg.getSummary())
        .build());
    });
  }
}
