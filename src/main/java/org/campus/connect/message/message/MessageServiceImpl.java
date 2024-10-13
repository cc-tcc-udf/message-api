package org.campus.connect.message.message;

import org.campus.connect.message.constants.Enums.Status;
import org.campus.connect.message.files.FileDTO;
import org.campus.connect.message.files.FileService;
import org.campus.connect.message.links.LinksDTO;
import org.campus.connect.message.links.LinksService;
import org.campus.connect.message.utils.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl extends GenericServiceImpl<Message, MessageDTO> implements MessageService {

  private final MessageRepository repository;
  private final MessageMapper mapper;
  private final FileService fileService;
  private final LinksService linksService;

  public MessageServiceImpl(
    final MessageRepository repository,
    final MessageMapper mapper, final FileService fileService, final LinksService linksService) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
    this.fileService = fileService;
    this.linksService = linksService;
  }

  @Override
  public MessageDTO findMsgById(final Long id) {
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
    if (Status.ENVIADO.equals(msg.getStatus())) {
      msg.setSendDate(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.of("-03:00")));
    }

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

    if (msg.getAttachments() != null && !msg.getAttachments().isEmpty()) {
      msg.getAttachments().forEach(attachment -> {
        try {
          FileDTO savedAttachment = this.fileService.save(attachment);
          attachment.setId(savedAttachment.getId());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    }

    return this.save(msg);
  }
}
