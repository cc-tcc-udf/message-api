package org.campus.connect.message.message;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.campus.connect.message.constants.Enums.Status;
import org.campus.connect.message.course.CourseDTO;
import org.campus.connect.message.course.CourseService;
import org.campus.connect.message.firebase.FirebaseMessageDTO;
import org.campus.connect.message.firebase.FirebaseService;
import org.campus.connect.message.links.LinksDTO;
import org.campus.connect.message.links.LinksService;
import org.campus.connect.message.utils.GenericServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class MessageServiceImpl extends GenericServiceImpl<Message, MessageDTO> implements MessageService {
  private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);


  private final MessageRepository repository;
  private final MessageMapper mapper;
  private final LinksService linksService;
  private final FirebaseService firebaseService;
  private final CourseService courseService;

  public MessageServiceImpl(
    final MessageRepository repository,
    final MessageMapper mapper,
    final LinksService linksService, final FirebaseService firebaseService,
    final CourseService courseService) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
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
    if (msg.getCourses().isEmpty()) {
      return null;
    }
    msg.setSendDate(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.of("-03:00")));
    msg.setStatus(Status.ENVIADO);
    MessageDTO obj = this.create(msg);
    this.firebaseSend(obj);
    return obj;
  }

  private List<List<String>> partitionList(List<String> tokens) {
    List<List<String>> partitions = new ArrayList<>();
    for (int i = 0; i < tokens.size(); i += 500) {
      partitions.add(tokens.subList(i, Math.min(i + 500, tokens.size())));
    }
    return partitions;
  }

  private void firebaseSend(final MessageDTO msg) throws ExecutionException, InterruptedException {
    for (CourseDTO c : msg.getCourses()) {
      CourseDTO group = courseService.findById(c.getCourseGroupId());
      String course = c.getName();
      List<FirebaseMessageDTO> tokens = firebaseService.fetchTokens(course, group.getName());
      List<String> notificationTokens = tokens.stream()
        .map(FirebaseMessageDTO::getNotificationToken)
        .toList();

      List<List<String>> tokenBatches = partitionList(notificationTokens);

      for (List<String> batch : tokenBatches) {
        try {
          this.firebaseService.sendMultiNotification(FirebaseMessageDTO.builder()
            .id(msg.getId().toString())
            .title(msg.getTitle())
            .body(msg.getSummary())
            .build(), batch);
        } catch (FirebaseMessagingException e) {
          logger.error("Erro ao enviar lote de notificações: {}", e.getMessage(), e);
        }
      }
    }
  }
}
