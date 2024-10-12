package org.campus.connect.message.message;

import org.campus.connect.message.constants.Enums.Status;
import org.campus.connect.message.files.FileService;
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
    MessageDTO msg = new MessageDTO(message);
    msg.setLinks(this.linksService.findByIdMsg(msg.getId()));
    msg.setAttachments(this.fileService.findAllByIdExt(msg.getId()));
    return msg;
  }

  @Override
  public List<MessageDTO> findAll() {
    List<MessageDTO> list = this.mapper.toDto(this.repository.findAll());
    list.forEach(m -> {
      m.setLinks(this.linksService.findByIdMsg(m.getId()));
      m.setAttachments(this.fileService.findAllByIdExt(m.getId()));
    });
    return list;
  }

  @Override
  public MessageDTO create(final MessageDTO msg) throws Exception {
    if (Status.ENVIADO.equals(msg.getStatus())) {
      msg.setSendDate(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.of("-03:00")));
    }

    MessageDTO message = this.save(msg);
    if (msg.getLinks() != null && !msg.getLinks().isEmpty()) {
      msg.getLinks().forEach(link -> {
        link.setId_msg(message.getId());
        try {
          this.linksService.create(link);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    }
    if (msg.getAttachments() != null && !msg.getAttachments().isEmpty()) {
      msg.getAttachments().forEach(attachment -> {
        attachment.setId_ext(message.getId());
        try {
          this.fileService.save(attachment);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    }
    return message;
  }
}
